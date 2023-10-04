<template>
<div class="search">
    <Card>
        <div class="operation">
            <Row>
                <Form ref="searchForm" :model="searchForm" inline :label-width="85">
                    <Form-item label="Original file name" prop="name">
                        <Input type="text" v-model="searchForm.name" placeholder="Please enter file name" clearable style="width: 160px" />
                    </Form-item>
                    <Form-item label="Uploader account" prop="createBy">
                        <Input type="text" v-model="searchForm.createBy" placeholder="Please enter the uploader's login account" clearable style="width: 160px" />
                    </Form-item>
                    <Form-item label="Store file name" prop="name">
                        <Input type="text" v-model="searchForm.fkey" placeholder="Please enter the storage file name" clearable style="width: 160px" />
                    </Form-item>
                    <Form-item label="Upload time">
                        <DatePicker v-model="selectDate" type="daterange" format="yyyy-MM-dd" clearable @on-change="selectDateRange" placeholder="Select start time" style="width: 160px"></DatePicker>
                    </Form-item>
                    <Form-item style="margin-left:-35px;" class="br">
                        <Button @click="handleSearch" ghost shape="circle" type="primary" icon="ios-search" size="small">Search</Button>
                        <Button @click="handleReset" ghost shape="circle" type="warning" icon="md-refresh" size="small">Reset</Button>
                        <Button @click="uploadVisible=true" ghost shape="circle" type="success" icon="md-cloud-upload" size="small" :disabled="!$route.meta.permTypes.includes('add')">Upload File</Button>
                        <Button @click="fileSaveSettingModal=true" ghost shape="circle" type="info" icon="md-settings" size="small" :disabled="!$route.meta.permTypes.includes('enable')">Settings</Button>
                    </Form-item>
                </Form>
            </Row>
        </div>
        <Row>
            <Table :loading="loading" border :columns="columns" :data="data" ref="table" sortable="custom" @on-sort-change="changeSort" @on-selection-change="changeSelect"></Table>
        </Row>
        <Row type="flex" justify="end" class="page">
            <Page :current="searchForm.pageNumber" :total="total" :page-size="searchForm.pageSize" @on-change="changePage" @on-page-size-change="changePageSize" :page-size-opts="[15, 20, 40]" size="small" show-total show-elevator show-sizer></Page>
        </Row>
    </Card>

    <Drawer title="File Upload" closable v-model="uploadVisible" width="500">
        <Upload action="/zwz/upload/file" :headers="accessToken" :on-success="handleSuccess" :on-error="handleError" :max-size="5120" :on-exceeded-size="handleMaxSize" :before-upload="beforeUpload" multiple type="drag" ref="up">
            <div style="padding: 20px 0">
                <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                <p>Click here or drag the file here to upload</p>
            </div>
        </Upload>
        <div class="drawer-footer">
            <Button @click="clearFiles">Clear upload list</Button>
        </div>
    </Drawer>

    <Modal title="File rename" v-model="changeFileNameModal" :mask-closable="false" :width="500" ok-text="Save" @on-ok="handleSubmit">
        <Form ref="form" :model="form" :label-width="95" :rules="formValidate">
            <Row :gutter="16">
                <Col span="24">
                <FormItem label="Original file name" prop="name">
                    <Input v-model="form.name" style="width:100%" />
                </FormItem>
                </Col>
            </Row>
            <Row :gutter="16">
                <Col span="24">
                <FormItem label="Store file name" prop="fkey">
                    <Input v-model="form.fkey" style="width:100%" />
                </FormItem>
                </Col>
            </Row>
        </Form>
    </Modal>

    <Modal title="File storage configuration" v-model="fileSaveSettingModal" :mask-closable="false" :width="700" ok-text="Save" @on-ok="setFileSettingFx">
        <Form ref="settingForm" :model="settingForm" :label-width="95">
            <Row :gutter="16">
                <Col span="24">
                <FormItem label="Storage path">
                    <Input type="text" v-model="filePath" placeholder="Such as C:\\oa-file" style="width: 100%" :disabled="changeLoading" />
                </FormItem>
                </Col>
            </Row>
            <Row :gutter="16">
                <Col span="24">
                <FormItem label="Preview interface">
                    <Input type="text" v-model="fileView" placeholder="如127.0.0.1:8080/zwz/file/view" style="width: 100%">
                    <Select v-model="fileHttp" slot="prepend" style="width: 80px" prop="http" :disabled="changeLoading">
                        <Option value="http://">http://</Option>
                        <Option value="https://">https://</Option>
                    </Select>
                    </Input>
                </FormItem>
                </Col>
            </Row>
        </Form>
    </Modal>

</div>
</template>

<script>
import {
    getFileListData,
    copyFile,
    renameFile,
    deleteFile,
    getOneSetting,
    setOneSetting
} from "./api.js";
import "viewerjs/dist/viewer.css";
import Viewer from "viewerjs";
var dp;
export default {
    name: "oss-manage",
    data() {
        return {
            filePath: "",
            fileHttp: "",
            fileView: "",
            fileSaveSettingModal: false,
            accessToken: {}, // 上传token鉴权
            loading: false, // 表单加载状态
            drop: false,
            dropDownContent: "Expand",
            dropDownIcon: "ios-arrow-down",
            fileType: "all",
            changeFileNameModal: false, // 添加或编辑显示
            uploadVisible: false,
            picVisible: false,
            picTitle: "",
            searchForm: {
                // 搜索框对应data对象
                name: "",
                fkey: "",
                type: "",
                pageNumber: 1, // 当前页数
                pageSize: 15, // 页面大小
                sort: "createTime", // 默认排序字段
                order: "desc", // 默认排序方式
                startDate: "", // 起始时间
                endDate: "" // 终止时间
            },
            selectDate: null, // 选择日期绑定modal
            form: {
                name: "",
                fkey: ""
            },
            file: {},
            // 表单验证规则
            formValidate: {
                name: [{
                    required: true,
                    message: "Can not be empty",
                    trigger: "blur"
                }],
                fkey: [{
                    required: true,
                    message: "Can not be empty",
                    trigger: "blur"
                }]
            },
            submitLoading: false,
            selectList: [],
            selectCount: 0,
            totalSize: "",
            columns: [{
                    type: "selection",
                    width: 60,
                    align: "center"
                },
                {
                    type: "index",
                    width: 60,
                    align: "center"
                },
                {
                    title: "Original file name",
                    key: "name",
                    minWidth: 130,
                    sortable: true
                },
                {
                    title: "Store file name",
                    key: "fkey",
                    minWidth: 165,
                    sortable: true
                },
                {
                    title: "Preview",
                    key: "url",
                    minWidth: 150,
                    align: "center",
                    render: (h, params) => {
                        if (params.row.type.includes("image") > 0) {
                            return h("img", {
                                attrs: {
                                    src: params.row.url
                                },
                                style: {
                                    cursor: "zoom-in",
                                    width: "80px",
                                    height: "60px",
                                    margin: "10px 0",
                                    "object-fit": "contain"
                                },
                                on: {
                                    click: () => {
                                        this.showPic(params.row);
                                    }
                                }
                            });
                        } else if (params.row.type.includes("pdf") > 0) {
                            return h("img", {
                                attrs: {
                                    src: require("@/assets/pdf.png")
                                },
                                style: {
                                    cursor: "zoom-in",
                                    width: "60px",
                                    margin: "10px 0",
                                    "object-fit": "contain"
                                },
                                on: {
                                    click: () => {
                                        this.showFile(params.row);
                                    }
                                }
                            });
                        } else if (
                            params.row.type.includes("zip") > 0 ||
                            params.row.type.includes("rar") > 0 ||
                            params.row.type.includes("7z") > 0
                        ) {
                            return h("img", {
                                attrs: {
                                    src: require("@/assets/zip.png")
                                },
                                style: {
                                    width: "60px",
                                    margin: "10px 0",
                                    "object-fit": "contain"
                                },
                                on: {
                                    click: () => {
                                        this.showFile(params.row);
                                        this.$Message.warning("Please download and unzip to access!");
                                    }
                                }
                            });
                        } else {
                            return h("img", {
                                attrs: {
                                    src: require("@/assets/file.png")
                                },
                                style: {
                                    width: "55px",
                                    height: "60px",
                                    margin: "10px 0",
                                    "object-fit": "contain"
                                },
                                on: {
                                    click: () => {
                                        this.showFile(params.row);
                                    }
                                }
                            });
                        }
                    }
                },
                {
                    title: "File type",
                    key: "type",
                    minWidth: 115,
                    sortable: true
                },
                {
                    title: "File size",
                    key: "size",
                    minWidth: 115,
                    sortable: true,
                    render: (h, params) => {
                        return h("div", [
                            h(
                                "Tag", {
                                    props: {
                                        color: (params.row.size > 1024 * 1024 * 5) ? "orange" : "green"
                                    }
                                },
                                ((params.row.size * 1.0) / (1024 * 1024)).toFixed(2) + " MB"
                            )
                        ]);
                    }
                },
                {
                    title: "Uploaded by",
                    key: "createBy",
                    minWidth: 120,
                    sortable: true,
                    align: "center",
                    render: (h, params) => {
                        return h(
                            "Tooltip", {
                                props: {
                                    placement: "top",
                                    content: params.row.createBy
                                }
                            },
                            [
                                h(
                                    "Tag", {
                                        style: {
                                            "margin-right": "8px"
                                        },
                                        props: {
                                            type: "border"
                                        }
                                    },
                                    params.row.nickname
                                )
                            ]
                        );
                    }
                },
                {
                    title: "Upload time",
                    key: "createTime",
                    minWidth: 180,
                    sortable: true,
                    sortType: "desc"
                },
                {
                    title: "Operate",
                    key: "action",
                    align: "center",
                    fixed: "right",
                    minWidth: 260,
                    render: (h, params) => {
                        var that = this;
                        return h("div", [
                            h(
                                "Button", {
                                    props: {
                                        type: "success",
                                        size: "small",
                                        ghost: true,
                                        shape: "circle"
                                    },
                                    style: {
                                        marginRight: "5px"
                                    },
                                    on: {
                                        click: () => {
                                            this.download(params.row);
                                        }
                                    }
                                },
                                "Download"
                            ),
                            h(
                                "Button", {
                                    props: {
                                        type: "warning",
                                        size: "small",
                                        ghost: true,
                                        shape: "circle"
                                    },
                                    style: {
                                        marginRight: "5px"
                                    },
                                    on: {
                                        click: () => {
                                            this.rename(params.row);
                                        }
                                    }
                                },
                                "Rename"
                            ),
                            h(
                                "Button", {
                                    props: {
                                        type: "info",
                                        size: "small",
                                        ghost: true,
                                        shape: "circle"
                                    },
                                    style: {
                                        marginRight: "5px"
                                    },
                                    on: {
                                        click: () => {
                                            this.copy(params.row);
                                        }
                                    }
                                },
                                "Copy"
                            ),
                            h(
                                "Button", {
                                    props: {
                                        type: "error",
                                        size: "small",
                                        ghost: true,
                                        shape: "circle",
                                        disabled: !(that.$route.meta.permTypes && that.$route.meta.permTypes.includes("delete"))
                                    },
                                    style: {
                                        marginRight: "5px"
                                    },
                                    on: {
                                        click: () => {
                                            this.remove(params.row);
                                        }
                                    }
                                },
                                "Delete"
                            )
                        ]);
                    }
                }
            ],
            data: [],
            total: 0,
        };
    },
    methods: {
        init() {
            var that = this;
            this.getDataList();
            this.getFileSettingFx();
            this.accessToken = {
                accessToken: this.getStore("accessToken")
            };
        },
        getFileSettingFx() {
            var that = this;
            getOneSetting({
                id: "FILE_PATH"
            }).then(res => {
                if (res.success) {
                    that.filePath = res.result.value
                }
            });
            getOneSetting({
                id: "FILE_HTTP"
            }).then(res => {
                if (res.success) {
                    that.fileHttp = res.result.value
                }
            });
            getOneSetting({
                id: "FILE_VIEW"
            }).then(res => {
                if (res.success) {
                    that.fileView = res.result.value
                }
            });
        },
        setFileSettingFx() {
            var that = this;
            setOneSetting({
                id: "FILE_PATH",
                value: that.filePath
            }).then(res => {
                if (res.success) {
                    this.$Message.success("Storage path updated successfully");
                }
            });
            setOneSetting({
                id: "FILE_HTTP",
                value: that.fileHttp
            }).then(res => {
                if (res.success) {
                    this.$Message.success("HTTP update successful");
                }
            });
            setOneSetting({
                id: "FILE_VIEW",
                value: that.fileView
            }).then(res => {
                if (res.success) {
                    this.$Message.success("Preview interface updated successfully");
                }
            });
        },
        showPic(v) {
            let image = new Image();
            image.src = v.url;
            let viewer = new Viewer(image, {
                hidden: function () {
                    viewer.destroy();
                }
            });
            viewer.show();
        },
        closeVideo() {
            dp.destroy();
        },
        dropDown() {
            if (this.drop) {
                this.dropDownContent = "Expand";
                this.dropDownIcon = "ios-arrow-down";
            } else {
                this.dropDownContent = "Close";
                this.dropDownIcon = "ios-arrow-up";
            }
            this.drop = !this.drop;
        },
        changePage(v) {
            this.searchForm.pageNumber = v;
            this.getDataList();
            this.clearSelectAll();
        },
        changePageSize(v) {
            this.searchForm.pageSize = v;
            this.getDataList();
        },
        changeSort(e) {
            this.searchForm.sort = e.key;
            this.searchForm.order = e.order;
            if (e.order == "normal") {
                this.searchForm.order = "";
            }
            this.getDataList();
        },
        selectDateRange(v) {
            if (v) {
                this.searchForm.startDate = v[0];
                this.searchForm.endDate = v[1];
            }
        },
        getDataList() {
            this.loading = true;
            getFileListData(this.searchForm).then(res => {
                this.loading = false;
                if (res.success) {
                    this.data = res.result.records;
                    this.total = res.result.total;
                }
            });
        },
        handleSearch() {
            this.searchForm.pageNumber = 1;
            this.searchForm.pageSize = 15;
            this.getDataList();
        },
        changeFileType() {
            let name = this.fileType;
            if (name == "all") {
                this.searchForm.type = "";
            } else if (name == "pic") {
                this.searchForm.type = "image";
            } else if (name == "video") {
                this.searchForm.type = "video";
            }
            this.handleSearch();
        },
        handleReset() {
            this.$refs.searchForm.resetFields();
            this.searchForm.pageNumber = 1;
            this.searchForm.pageSize = 15;
            this.selectDate = null;
            this.searchForm.startDate = "";
            this.searchForm.endDate = "";
            this.getDataList();
        },
        beforeUpload() {
            return true;
        },
        handleMaxSize(file) {
            this.$Notice.warning({
                title: "File size too large",
                desc: "The selected file ‘" + file.name + "’ is too large, it must not exceed 5M."
            });
        },
        handleSuccess(res, file) {
            if (res.success) {
                this.$Message.success("Upload file " + file.name + " successful");
                this.getDataList();
            } else {
                this.$Message.error(res.message);
            }
        },
        handleError(error, file, fileList) {
            this.$Message.error(error.toString());
        },
        clearFiles() {
            this.$refs.up.clearFiles();
        },
        download(v) {
            window.open(
                v.url +
                "?attname=&response-content-type=application/octet-stream&filename=" +
                v.name
            );
        },
        showFile(v) {
            window.open(v.url + "?preview=true");
        },
        copy(v) {
            this.$Modal.confirm({
                title: "Confirm copy",
                content: "You confirm you want to copy the file " + v.name + " ?",
                loading: true,
                onOk: () => {
                    copyFile({
                        id: v.id,
                        key: v.fkey
                    }).then(res => {
                        this.$Modal.remove();
                        if (res.success) {
                            this.$Message.success(
                                "The file was copied successfully and the new file name is " + v.name + "_2"
                            );
                            this.getDataList();
                        }
                    });
                }
            });
        },
        removeAll() {
            if (this.selectCount <= 0) {
                this.$Message.warning("You have not selected data to delete");
                return;
            }
            this.$Modal.confirm({
                title: "Confirm deletion",
                content: "Are you sure you want to delete the selected " + this.selectCount + " files?",
                loading: true,
                onOk: () => {
                    let ids = "";
                    this.selectList.forEach(function (e) {
                        ids += e.id + ",";
                    });
                    ids = ids.substring(0, ids.length - 1);
                    deleteFile({
                        ids: ids
                    }).then(res => {
                        this.$Modal.remove();
                        if (res.success) {
                            this.$Message.success("Batch deletion of files successfully");
                            this.clearSelectAll();
                            this.getDataList();
                        }
                    });
                }
            });
        },
        remove(v) {
            this.$Modal.confirm({
                title: "Confirm deletion",
                content: "You confirm you want to delete the file " + v.name + " ?",
                loading: true,
                onOk: () => {
                    deleteFile({
                        ids: v.id
                    }).then(res => {
                        this.$Modal.remove();
                        if (res.success) {
                            this.$Message.success("Delete file " + v.name + " success");
                            this.getDataList();
                        }
                    });
                }
            });
        },
        handleSubmit() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    this.submitLoading = true;
                    let params = {
                        id: this.form.id,
                        newKey: this.form.fkey,
                        newName: this.form.name
                    };
                    renameFile(params).then(res => {
                        this.submitLoading = false;
                        if (res.success) {
                            this.$Message.success("Successful operation");
                            this.getDataList();
                            this.changeFileNameModal = false;
                        }
                    });
                }
            });
        },
        rename(v) {
            for (let attr in v) {
                if (v[attr] == null) {
                    v[attr] = "";
                }
            }
            let str = JSON.stringify(v);
            let data = JSON.parse(str);
            this.form = data;
            this.changeFileNameModal = true;
        },
        clearSelectAll() {
            this.$refs.table.selectAll(false);
            this.totalSize = "";
        },
        changeSelect(e) {
            this.selectList = e;
            this.selectCount = e.length;
            let size = 0;
            e.forEach(item => {
                size += item.size * 1.0;
            });
            this.totalSize = ((size * 1.0) / (1024 * 1024)).toFixed(2) + " MB";
        }
    },
    mounted() {
        this.init();
    }
};
</script>

<style lang="less">
@import "../../../styles/table-common.less";

.search {
    .oss-operation {
        margin-bottom: 2vh;
        display: flex;
        align-items: center;
        justify-content: space-between;
        width: 100%;
    }
}

.oss-wrapper {
    display: flex;
    flex-wrap: wrap;
    position: relative;
}

.oss-card {
    margin: 10px 2.5% 10px 0;
    width: 22.5%;

    :hover {
        .content .other .name {
            color: #1890ff;
            transition: color .3s;
        }
    }

    cursor: pointer;

    .ivu-card-body {
        padding: 0;
    }

    .content {
        display: flex;
        flex-direction: column;

        :hover {
            .play {
                transition: opacity .3s;
                opacity: 1 !important;
            }
        }

        .img {
            height: 135px;
            object-fit: cover;
        }

        .video {
            height: 135px;
            position: relative;

            .cover {
                height: 100%;
                width: 100%;
                object-fit: fill;
            }

            .play {
                position: absolute;
                top: 43px;
                left: 117px;
                height: 50px;
                width: 50px;
                opacity: 0.8;
            }
        }

        .other {
            padding: 16px;
            height: 135px;

            .name {
                font-size: 16px;
                text-overflow: ellipsis;
                overflow: hidden;
                white-space: nowrap;
                color: rgba(0, 0, 0, .85);
                font-weight: 500;
                margin-bottom: 4px;
            }

            .key {
                overflow: hidden;
                text-overflow: ellipsis;
                height: 45px;
                word-break: break-all;
                color: rgba(0, 0, 0, .45);
            }

            .info {
                font-size: 12px;
                color: rgba(0, 0, 0, .45);
                overflow: hidden;
                text-overflow: ellipsis;
                height: 36px;
                word-break: break-all;
            }
        }

        .actions {
            display: flex;
            align-items: center;
            height: 50px;
            background: #f7f9fa;
            border-top: 1px solid #e8e8e8;

            i:hover {
                color: #1890ff;
            }

            .btn {
                display: flex;
                justify-content: center;
                width: 25%;
                border-right: 1px solid #e8e8e8;
            }

            .btn-no {
                display: flex;
                justify-content: center;
                width: 25%;
            }
        }
    }
}
</style>
