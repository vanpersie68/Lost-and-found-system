<template>
<div class="search">
    <add v-if="currView=='add'" @close="currView='index'" @submited="submited" />
    <edit v-if="currView=='edit'" @close="currView='index'" @submited="submited" :data="formData" />
    <Card v-show="currView=='index'">
        <Row v-show="openSearch" @keydown.enter.native="handleSearch">
            <Form ref="searchForm" :model="searchForm" inline :label-width="0">
                <Form-item ref="searchForm" :model="searchForm" inline :label-width="0" style="display:flex;">
                    <Form-item label="" prop="title">
                        <Input type="text" v-model="searchForm.title" placeholder="Item Name" clearable style="width: 150px" />
                    </Form-item>
                    <Form-item label="" prop="content">
                        <Input type="text" v-model="searchForm.content" placeholder="Related description" clearable style="width: 150px" />
                    </Form-item>
                    <Form-item style="margin-left:10px;" class="br">
                        <Button @click="handleSearch" type="primary" icon="ios-search" size="small" ghost>Search</Button>
                        <Button @click="handleReset" type="warning" size="small" icon="md-refresh" ghost>Reset</Button>
                        <Button @click="add" type="info" size="small" icon="md-add" ghost :disabled="!$route.meta.permTypes.includes('add')">Add</Button>
                        <Button @click="delAll" type="error" icon="md-trash" size="small" ghost :disabled="!$route.meta.permTypes.includes('delete')">Delete</Button>
                        <Button @click="excelData" type="success" icon="md-paper-plane" size="small" ghost>Export</Button>
                    </Form-item>
                    <Form-item style="position:fixed;right:50px;top:130px">
                        <Button type="info" @click="showFilterPanelFlag = !showFilterPanelFlag" class="showFilterPanelFlag" icon="md-settings" size="small" ghost>
                            Column filter</Button>
                        <Button type="warning" @click="modal1 = true" class="showFilterPanelFlag" icon="ios-help-circle-outline" size="small" ghost>
                            User guide</Button>
                        <Modal v-model="modal1" title="User guide">
                            <p>1.XXXXXXXXXXXXXXXXXXXXXXXX</p>
                            <p>2.XXXXXXXXXXXXXXXXXXXXXXXX</p>
                            <p>3.XXXXXXXXXXXXXXXXXXXXXXXX</p>
                        </Modal>
                    </Form-item>
                </Form-item>
            </Form>
        </Row>
        <Row class="operation" style="position:relative;">
            <transition>
                <div v-show="showFilterPanelFlag" class="filter-panel">
                    <CheckboxGroup v-model="selected">
                        <div v-for="item in mycolumns" :key="item.key">
                            <Checkbox :label="item.title" style="margin: 2px 5px"></Checkbox>
                        </div>
                    </CheckboxGroup>
                </div>
            </transition>
        </Row>
        <Row v-show="openTip"> </Row>
        <Row>
            <Table :loading="loading" :height="tableHeight" border stripe size="small" :columns="columns" :data="data" ref="table" sortable="custom" @on-sort-change="changeSort" @on-selection-change="changeSelect" @on-row-click="rowClick" :row-class-name="rowClassNmae"></Table>
        </Row>
        <Row type="flex" justify="end" class="page">
            <Page :current="searchForm.pageNumber" :total="total" :page-size="searchForm.pageSize" @on-change="changePage" @on-page-size-change="changePageSize" :page-size-opts="[15,20,50]" size="small" show-total show-elevator show-sizer></Page>
        </Row>
    </Card>
</div>
</template>

<script>
import {
    getDrawList,
    deleteDraw
} from "./api.js";
import add from "./add.vue";
import edit from "./edit.vue";
export default {
    name: "single-window",
    components: {
        add,
        edit
    },
    data() {
    return {
        tableHeight: 0,
        selected: [
            "Select",
            "Serial Number",
            "Item Name",
            "Found Time",
            "Found Location",
            "Type",
            "Related Description",
            "Temporary Storage Location",
            "Item Image",
            "Creation Time",
            "Creator",
            "Operation",
        ],
        modal1: false,
        openSearch: true, // Show search
        openTip: true, // Show tip
        formData: {},
        currView: "index",
        loading: true, // Form loading state
        searchForm: { // Search box initialization object
            pageNumber: 1, // Current page number
            pageSize: 15, // Page size
            sort: "createTime", // Default sort field
            order: "desc", // Default sort order
        },
        selectList: [], // Multi-select data
        selectCount: 0, // Multi-select count
        selectRow: 0,
        columns: [
            // Table header
            {
                type: "selection",
                width: 60,
                title: "Select",
                align: "center",
                fixed: "left",
            },
            {
                title: "Serial Number",
                width: 85,
                align: "center",
                fixed: "left",
                sortType: true,
                render: (h, params) => {
                    return h(
                        "span",
                        params.index +
                        (this.searchForm.pageNumber - 1) * this.searchForm.pageSize +
                        1
                    );
                },
            },
            {
                title: "Item Name",
                key: "title",
                minWidth: 180,
                tooltip: true,
                sortable: false,
            },
            {
                title: "Found Time",
                key: "time",
                minWidth: 180,
                tooltip: true,
                sortable: false,
            },
            {
                title: "Found Location",
                key: "address",
                minWidth: 180,
                tooltip: true,
                sortable: false,
            },
            {
                title: "Type",
                key: "type",
                minWidth: 180,
                tooltip: true,
                sortable: false,
            },
            {
                title: "Related Description",
                key: "content",
                minWidth: 180,
                tooltip: true,
                sortable: false,
            },
            {
                title: "Temporary Storage Location",
                key: "pushAddress",
                minWidth: 180,
                tooltip: true,
                sortable: false,
            },
            {
                title: "Item Image",
                key: "image",
                minWidth: 180,
                tooltip: true,
                sortable: false,
                render: (h, params) => {
                    if (params.row.image == undefined || params.row.image == "") {
                        return h("div", [
                            h(
                                "span", {
                                    style: {
                                        color: "#ff9900",
                                    },
                                },
                                "Not Uploaded"
                            ),
                        ]);
                    } else {
                        return h("img", {
                            attrs: {
                                src: params.row.image,
                            },
                            style: {
                                cursor: "zoom-in",
                                width: "60px",
                                margin: "10px 0",
                                "object-fit": "contain",
                            },
                            on: {
                                click: () => {
                                    this.showFile(params.row.image);
                                },
                            },
                        });
                    }
                }
            },
            {
                title: "Creation Time",
                key: "createTime",
                sortable: true,
                sortType: "desc",
                minWidth: 180,
                align: "center",
                tooltip: true,
            },
            {
                title: "Creator",
                key: "createBy",
                sortable: true,
                sortType: "desc",
                minWidth: 100,
                align: "center",
                tooltip: true,
            },
            {
                title: "Modification Time",
                key: "updateTime",
                minWidth: 180,
                align: "center",
                tooltip: true,
            },
            {
                title: "Modifier",
                key: "updateBy",
                minWidth: 100,
                align: "center",
                tooltip: true,
            },
            {
                title: "Operation",
                key: "action",
                align: "center",
                width: 200,
                fixed: "right",
                render: (h, params) => {
                    var that = this;
                    return h("div", [
                        h(
                            "Button", {
                                props: {
                                    type: "primary",
                                    size: "small",
                                    icon: "ios-create-outline",
                                    ghost: true,
                                    disabled: !(that.$route.meta.permTypes && that.$route.meta.permTypes.includes("edit"))
                                },
                                style: {
                                    marginRight: "5px"
                                },
                                on: {
                                    click: () => {
                                        this.edit(params.row);
                                    }
                                }
                            },
                            "Edit"
                        ),
                        h(
                            "Button", {
                                props: {
                                    type: "error",
                                    size: "small",
                                    icon: "md-trash",
                                    ghost: true,
                                    disabled: !(that.$route.meta.permTypes && that.$route.meta.permTypes.includes("delete"))
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
            data: [], // 表单数据
            pageNumber: 1, // 当前页数
            pageSize: 10, // 页面大小
            total: 0, // 表单数据总数
            showFilterPanelFlag: false,
        };
    },
    methods: {
        init() {
            this.getDataList();
        },
        submited() {
            this.currView = "index";
            this.getDataList();
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
        rowClick(row, index) {
            this.selectRow = row;
        },
        rowClassNmae(row, index) {
            if (row.id == this.selectRow.id) {
                return "rowClassNmaeColor";
            }
            return "";
        },
        excelData() {
            this.$refs.table.exportCsv({
                filename: "Export results",
            });
        },
        handleSearch() {
            this.searchForm.pageNumber = 1;
            this.searchForm.pageSize = 15;
            this.getDataList();
        },
        handleReset() {
            this.$refs.searchForm.resetFields();
            this.searchForm.pageNumber = 1;
            this.searchForm.pageSize = 15;
            // 重新加载数据
            this.getDataList();
        },
        changeSort(e) {
            this.searchForm.sort = e.key;
            this.searchForm.order = e.order;
            if (e.order === "normal") {
                this.searchForm.order = "";
            }
            this.getDataList();
        },
        clearSelectAll() {
            this.$refs.table.selectAll(false);
        },
        changeSelect(e) {
            this.selectList = e;
            this.selectCount = e.length;
        },
        getDataList() {
            this.loading = true;
            getDrawList(this.searchForm).then(res => {
                this.loading = false;
                if (res.success) {
                    this.data = res.result.records;
                    this.total = res.result.total;
                }
            });
        },
        add() {
            this.currView = "add";
        },
        edit(v) {
            // 转换null为""
            for (let attr in v) {
                if (v[attr] == null) {
                    v[attr] = "";
                }
            }
            let str = JSON.stringify(v);
            let data = JSON.parse(str);
            this.formData = data;
            this.currView = "edit";
        },
        remove(v) {
            this.$Modal.confirm({
                title: "confirm deletion",
                // 记得确认修改此处
                content: "Are you sure you want to delete?",
                loading: true,
                onOk: () => {
                    // 删除
                    deleteDraw({
                        ids: v.id
                    }).then(res => {
                        this.$Modal.remove();
                        if (res.success) {
                            this.$Message.success("Successful operation");
                            this.getDataList();
                        }
                    });
                }
            });
        },
        delAll() {
            if (this.selectCount <= 0) {
                this.$Message.warning("You have not selected data to delete");
                return;
            }
            this.$Modal.confirm({
                title: "confirm deletion",
                content: "Are you sure you want to delete the selected " + this.selectCount + " data items?",
                loading: true,
                onOk: () => {
                    let ids = "";
                    this.selectList.forEach(function (e) {
                        ids += e.id + ",";
                    });
                    ids = ids.substring(0, ids.length - 1);
                    // 批量删除
                    deleteDraw({
                        ids: ids
                    }).then(res => {
                        this.$Modal.remove();
                        if (res.success) {
                            this.$Message.success("Successful operation");
                            this.clearSelectAll();
                            this.getDataList();
                        }
                    });
                }
            });
        },
        showFile(e) {
            window.open(e + "?preview=true");
        }
    },
    mounted() {
        this.init();
        this.tableHeight = Number(window.innerHeight - 273);
        this.mycolumns = this.columns;
        let showcolumns = [];
        for (var i = 0; i < this.selected.length; i++) {
            var item = this.selected[i];
            for (var j = 0; j < this.columns.length; j++) {
                if (this.columns[j].title == item) {
                    showcolumns.push(this.columns[j]);
                }
            }
        }
        this.columns = showcolumns;
    },
    watch: {
        selected: function (newcolumns) {
            let showcolumns = [];
            for (var i = 0; i < this.mycolumns.length; i++) {
                var item = this.mycolumns[i];
                if (item.title == undefined) showcolumns.push(item);
                else if (newcolumns.includes(item.title)) showcolumns.push(item);
            }
            this.columns = showcolumns;
        },
    },
};
</script>

<style lang="less">
// @import "../../../styles/table-common.less";
.search {
    .operation {
        margin-bottom: 2vh;
    }

    .select-count {
        font-weight: 600;
        color: #40a9ff;
    }

    .select-clear {
        margin-left: 10px;
    }

    .page {
        margin-top: 2vh;
    }

    .drop-down {
        margin-left: 5px;
    }
}

.filter-panel {
    width: 166px;
    min-height: 120px;
    height: 200px;
    position: absolute;
    background-color: white;
    z-index: 9999;
    margin-left: 1px;
    overflow-y: scroll;
    border: 1px solid blue;
    top: 35px;
    right: 10px;
}

.openSearch {
    position: absolute;
    right: 240px;
}

.openTip {
    position: absolute;
    right: 130px;
}

.showFilterPanelFlag {
    position: static !important;
    right: 10px;
    margin-right: 10px;
}

.ivu-table td {
    height: 38px !important;
}

.ivu-table-cell-with-expand {
    height: 38px !important;
    line-height: 38px !important;
}

.ivu-table .rowClassNmaeColor td {
    background-color: #b0b3b6 !important;
    color: #ffffff !important;
    font-size: 12px;
}
</style>
