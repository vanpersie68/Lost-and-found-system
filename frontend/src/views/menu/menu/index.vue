<template>
    <div class="search">
        <Card>
            <Row :gutter="16">
                <Col span="7">
                <Row :gutter="16">
                    <Col span="23">
                    <div class="tree-bar" style="overflow-y: scroll;padding-right: 50px;" :style="{maxHeight: maxHeight + 'px'}">
                        <Tree ref="tree" :data="data" show-checkbox :render="renderContent" @on-select-change="selectTree" @on-check-change="changeSelect" :check-strictly="!strict"></Tree>
                        <Spin size="large" fix v-if="loading"></Spin>
                    </div>
                    </Col>
                </Row>
                </Col>
                <Col span="15">
                <Row class="operation">
                    <Col span="12">
                    <Button @click="addMenu" type="primary" shape="circle" ghost icon="md-add" :disabled="!$route.meta.permTypes.includes('add')">Add Menu</Button>
                    <Button @click="deletePermissionFx" type="error" shape="circle" ghost icon="md-trash" :disabled="!$route.meta.permTypes.includes('delete')">Delete Menu</Button>
                    <Button @click="getPermissionList" type="success" shape="circle" ghost icon="md-trash">Refresh Menu</Button>
                    <i-switch v-model="strict" size="large" style="margin-left:5px">
                        <span slot="open">Cascade</span>
                        <span slot="close">Single Select</span>
                    </i-switch>
                    </Col>
                    <Col span="12">
                    <Input v-model="searchKey" suffix="ios-search" @on-change="search" placeholder="Simulate Search Menu" clearable />
                    </Col>
                </Row>
                <Divider dashed />
                <Row :gutter="16" v-show="form.id">
                    <Col span="24">
                    <Alert show-icon type="success">
                        Current selected menu:
                        <span class="select-title">{{editTitle}}</span>
                        <Divider type="vertical" />
                        <a class="select-clear" @click="cancelEdit">Cancel Selection</a>
                    </Alert>
                    </Col>
                    <Divider dashed />
                </Row>
                <Form ref="form" :model="form" :label-width="130" :rules="formValidate">
                    <Row :gutter="16">
                        <Col span="12">
                        <FormItem label="Type" prop="type">
                            <div v-show="form.type==-1">
                                <Icon type="ios-navigate-outline" size="16" style="margin-right:5px;"></Icon>
                                <span>Top Menu</span>
                            </div>
                            <div v-show="form.type==0">
                                <Icon type="ios-list-box-outline" size="16" style="margin-right:5px;"></Icon>
                                <span>Page Menu</span>
                            </div>
                            <div v-show="form.type==1">
                                <Icon type="md-radio-button-on" size="16" style="margin-right:5px;"></Icon>
                                <span>Button-Level Permission</span>
                            </div>
                        </FormItem>
                        </Col>
                        <Col span="12" v-show="form.type==-1||form.type==0">
                        <FormItem label="Name" prop="title">
                            <Input v-model="form.title" style="width:100%" />
                        </FormItem>
                        </Col>
                        <Col span="12" v-show="form.type==1">
                        <FormItem label="Name" prop="title">
                            <Input v-model="form.title" placeholder="Operation button name must be unique" style="width:100%" />
                        </FormItem>
                        </Col>
                        <Col span="12" v-show="form.type==0">
                        <FormItem label="Path" prop="path">
                            <Input v-model="form.path" @on-blur="changeFormPath" style="width:100%" />
                        </FormItem>
                        </Col>
                        <Col span="12" v-show="form.type==1">
                        <FormItem label="Button Permission Type" prop="buttonType">
                            <Select v-model="form.buttonType" placeholder="Select or enter search" filterable clearable style="width:100%">
                                <Option v-for="(item, i) in dictPermissions" :key="i" :value="item.value">{{item.title}}</Option>
                            </Select>
                        </FormItem>
                        </Col>
                        <Col span="12" v-show="form.type==-1">
                        <FormItem :label="form.type==-1? 'English Name' : 'Route English Name'" prop="name">
                            <Input v-model="form.name" @on-blur="changeFormName" placeholder="Must be unique" style="width:100%" />
                        </FormItem>
                        </Col>
                        <Col span="12" v-show="form.type==-1||form.type==0">
                        <FormItem label="Icon" prop="icon">
                            <icon-choose v-model="form.icon"></icon-choose>
                        </FormItem>
                        </Col>
                        <Col span="12" v-show="form.type==0">
                        <FormItem label="Corresponding Location" prop="component">
                            <Input v-model="form.component" style="width:100%" />
                        </FormItem>
                        </Col>
                        <Col span="12">
                        <FormItem label="Sort Order" prop="sortOrder">
                            <InputNumber :max="1000" :min="0" v-model="form.sortOrder" style="width:100%"></InputNumber>
                        </FormItem>
                        </Col>
                        <Col span="12">
                        <FormItem label="Enabled" prop="status">
                            <i-switch size="large" v-model="form.status" :true-value="0" :false-value="-1">
                                <span slot="open">Enabled</span>
                                <span slot="close">Disabled</span>
                            </i-switch>
                        </FormItem>
                        </Col>
                        <Col span="12">
                        <Form-item>
                            <Button style="margin-right:5px" @click="submitEdit" ghost shape="circle" :loading="submitLoading" type="primary" icon="ios-create-outline" :disabled="!$route.meta.permTypes.includes('edit')">Save Menu</Button>
                        </Form-item>
                        </Col>
                    </Row>
                </Form>
                <Divider>Users with {{form.title}} permission</Divider>
                <Row :gutter="16">
                    <Table :loading="userByPermissionLoading" border stripe size="small" :columns="userByPermissionColumns" :data="userByPermissionList"></Table>
                </Row>
                </Col>
            </Row>
        </Card>
    
        <Modal draggable :title="modalTitle" v-model="menuModalVisible" :mask="false" :width="500" :styles="{top: '30px'}">
            <Form ref="formAdd" :model="formAdd" :label-width="100" :rules="formValidate">
                <div v-if="showParent">
                    <FormItem label="Parent Node:">{{parentTitle}}</FormItem>
                </div>
                <FormItem label="Type" prop="type">
                    <div v-show="formAdd.type==-1">
                        <Icon type="ios-navigate-outline" size="16" style="margin-right:5px;"></Icon>
                        <span>Top Menu</span>
                    </div>
                    <div v-show="formAdd.type==0">
                        <Icon type="ios-list-box-outline" size="16" style="margin-right:5px;"></Icon>
                        <span>Page Menu</span>
                    </div>
                    <div v-show="formAdd.type==1">
                        <Icon type="md-radio-button-on" size="16" style="margin-right:5px;"></Icon>
                        <span>Operation Button</span>
                    </div>
                </FormItem>
                <FormItem label="Name" prop="title" v-if="formAdd.type==-1||formAdd.type==0">
                    <Input v-model="formAdd.title" />
                </FormItem>
                <FormItem label="Name" prop="title" v-if="formAdd.type==1" class="block-tool">
                    <Tooltip placement="right" content="Operation button name must be unique">
                        <Input v-model="formAdd.title" />
                    </Tooltip>
                </FormItem>
                <FormItem label="Path" prop="path" v-if="formAdd.type==0">
                    <Input v-model="formAdd.path" />
                </FormItem>
                <FormItem label="Button Permission Type" prop="buttonType" v-if="formAdd.type==1">
                    <Select v-model="formAdd.buttonType" placeholder="Select or enter search" filterable clearable>
                        <Option v-for="(item, i) in dictPermissions" :key="i" :value="item.value">{{item.title}}</Option>
                    </Select>
                </FormItem>
                <FormItem label="English Name" prop="name" v-if="formAdd.type==-1" class="block-tool">
                    <Tooltip placement="right" content="Must be unique">
                        <Input v-model="formAdd.name" />
                    </Tooltip>
                </FormItem>
                <FormItem label="Route English Name" prop="name" v-if="formAdd.type==0" class="block-tool">
                    <Tooltip placement="right" content="Must be unique">
                        <Input v-model="formAdd.name" />
                    </Tooltip>
                </FormItem>
                <FormItem label="Icon" prop="icon" v-if="formAdd.type==-1||formAdd.type==0">
                    <icon-choose v-model="formAdd.icon"></icon-choose>
                </FormItem>
                <FormItem label="Corresponding Location" prop="component" v-if="formAdd.type==0">
                    <Input v-model="formAdd.component" />
                </FormItem>
                <FormItem label="Sort Order" prop="sortOrder">
                    <InputNumber :max="1000" :min="0" v-model="formAdd.sortOrder"></InputNumber>
                </FormItem>
                <FormItem label="Enabled" prop="status">
                    <i-switch size="large" v-model="formAdd.status" :true-value="0" :false-value="-1">
                        <span slot="open">Enabled</span>
                        <span slot="close">Disabled</span>
                    </i-switch>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="text" @click="menuModalVisible = false">Cancel</Button>
                <Button type="primary" :loading="submitLoading" @click="submitAdd">Submit</Button>
            </div>
        </Modal>
    </div>
    </template>
    
    <script>
    import {
        addPermission,
        editPermission,
        deletePermission,
        searchPermission,
        getDictDataByType,
        getUserByPermission
    } from "./api.js";
    import IconChoose from "@/views/template/icon-choose";
    import util from "@/libs/util.js";
    export default {
        name: "menu",
        components: {
            IconChoose
        },
        data() {
            return {
                userByPermissionLoading: false,
                loading: true,
                strict: true,
                maxHeight: 500,
                menuModalVisible: false,
                iconModalVisible: false,
                selectList: [],
                selectCount: 0,
                showParent: false,
                searchKey: "",
                parentTitle: "",
                editTitle: "",
                modalTitle: "",
                form: {
                    id: "",
                    title: "",
                    name: "",
                    icon: "",
                    path: "",
                    component: "",
                    parentId: "",
                    buttonType: "",
                    type: 0,
                    sortOrder: 0,
                    level: 0,
                    status: 0,
                    url: ""
                },
                formAdd: {
                    buttonType: ""
                },
                formValidate: {
                    title: [{
                        required: true,
                        message: "Menu name cannot be empty",
                        trigger: "blur"
                    }],
                    name: [{
                        required: true,
                        message: "Route English name cannot be empty",
                        trigger: "blur"
                    }],
                    icon: [{
                        required: true,
                        message: "Icon cannot be empty",
                        trigger: "click"
                    }],
                    path: [{
                        required: true,
                        message: "Path cannot be empty",
                        trigger: "blur"
                    }],
                    component: [{
                        required: true,
                        message: "Corresponding location cannot be empty",
                        trigger: "blur"
                    }],
                    sortOrder: [{
                        required: true,
                        type: "number",
                        message: "Sort order cannot be empty",
                        trigger: "blur"
                    }]
                },
                submitLoading: false,
                data: [],
                dictPermissions: [],
                userByPermissionList: [],
                userByPermissionColumns: [{
                    title: "Username",
                    key: "userName",
                    minWidth: 120,
                    tooltip: true,
                    sortable: false,
                }, {
                    title: "Corresponding Role",
                    key: "roleStr",
                    minWidth: 200,
                    tooltip: true,
                    sortable: false,
                }, {
                    title: "Login Account",
                    key: "code",
                    minWidth: 120,
                    tooltip: true,
                    sortable: false,
                }, {
                    title: "User Phone",
                    key: "mobile",
                    minWidth: 120,
                    tooltip: true,
                    sortable: false,
                }, {
                    title: "User Identifier",
                    key: "userId",
                    minWidth: 120,
                    tooltip: true,
                    sortable: false,
                }]
            };
        },
        methods: {
            init() {
                this.getPermissionList();
                this.getDictPermissions();
            },
            changeFormPath() {
                this.form.name = this.form.path;
            },
            changeFormName() {
                this.form.path = this.form.name;
            },
            getDictPermissions() {
                getDictDataByType("permission_type").then(res => {
                    if (res.success) {
                        this.dictPermissions = res.result;
                    }
                });
            },
            renderContent(h, {
                root,
                node,
                data
            }) {
                let icon = "";
                if (data.level == 0) {
                    icon = "ios-navigate";
                } else if (data.level == 1) {
                    icon = "md-list-box";
                } else if (data.level == 2) {
                    icon = "md-list";
                } else if (data.level == 3) {
                    icon = "md-radio-button-on";
                } else {
                    icon = "md-radio-button-off";
                }
                return h("span", [
                    h("span", [
                        h("Icon", {
                            props: {
                                type: icon,
                                size: "16"
                            },
                            style: {
                                "margin-right": "8px",
                                "margin-bottom": "3px"
                            }
                        }),
                        h("span", data.title)
                    ])
                ]);
            },
            getPermissionList() {
                this.loading = true;
                this.getRequest("/permission/getAllList").then(res => {
                    this.loading = false;
                    if (res.success) {
                        this.data = res.result;
                    }
                });
            },
            search() {
                if (this.searchKey) {
                    this.loading = true;
                    searchPermission({
                        title: this.searchKey
                    }).then(res => {
                        this.loading = false;
                        if (res.success) {
                            this.data = res.result;
                        }
                    });
                } else {
                    this.getPermissionList();
                }
            },
            selectTree(v) {
                if (v.length > 0) {
                    // Convert null to ""
                    for (let attr in v[0]) {
                        if (v[0][attr] == null) {
                            v[0][attr] = "";
                        }
                    }
                    let str = JSON.stringify(v[0]);
                    let menu = JSON.parse(str);
                    this.form = menu;
                    this.editTitle = menu.title;
                    this.getUserByPermission();
                } else {
                    this.cancelEdit();
                    this.userByPermissionList = [];
                }
            },
            getUserByPermission() {
                var that = this;
                that.userByPermissionLoading = true;
                getUserByPermission({
                    permissionId: that.form.id
                }).then(res => {
                    that.userByPermissionLoading = false;
                    if (res.success) {
                        that.userByPermissionList = res.result;
                    }
                })
            },
            cancelEdit() {
                let data = this.$refs.tree.getSelectedNodes()[0];
                if (data) {
                    data.selected = false;
                }
                this.$refs.form.resetFields();
                this.form.id = "";
                this.editTitle = "";
            },
            handleReset() {
                let type = this.form.type;
                this.$refs.form.resetFields();
                this.form.icon = "";
                this.form.component = "";
                this.form.type = type;
            },
            submitEdit() {
                if (!this.form.id) {
                    this.$Message.warning("Please select the menu node to be edited first");
                    return;
                }
                this.submitLoading = true;
                if (this.form.sortOrder == null) {
                    this.form.sortOrder = "";
                }
                if (this.form.buttonType == null) {
                    this.form.buttonType = "";
                }
                if (this.form.type == 1) {
                    this.form.name = "";
                    this.form.icon = "";
                    this.form.component = "";
                }
                delete this.form.updateTime;
                delete this.form.children;
                editPermission(this.form).then(res => {
                    this.submitLoading = false;
                    if (res.success) {
                        this.$Message.success("Edit successful");
                        // Mark to retrieve menu data again
                        this.$store.commit("setAdded", false);
                        util.initRouter(this);
                        this.init();
                        this.menuModalVisible = false;
                    }
                });
            },
            submitAdd() {
                this.$refs.formAdd.validate(valid => {
                    if (valid) {
                        this.submitLoading = true;
                        if (this.formAdd.type == 1) {
                            this.formAdd.name = "";
                            this.formAdd.icon = "";
                            this.formAdd.component = "";
                        }
                        addPermission(this.formAdd).then(res => {
                            this.submitLoading = false;
                            if (res.success) {
                                this.$Message.success("Add successful");
                                this.$store.commit("setAdded", false);
                                util.initRouter(this);
                                this.init();
                                this.menuModalVisible = false;
                            }
                        });
                    }
                });
            },
            addMenu() {
                if (!this.form.id) {
                    this.modalTitle = "Add Top Menu (Draggable)";
                    this.showParent = false;
                    this.formAdd = {
                        type: -1,
                        level: 0,
                        sortOrder: 0,
                        status: 0
                    };
                    this.menuModalVisible = true;
                    return;
                }
                this.parentTitle = this.form.title;
                this.modalTitle = "Add Child Node";
                this.showParent = true;
                let type = 0;
                if (this.form.level == 1) {
                    type = 0;
                } else if (this.form.level == 2) {
                    type = 1;
                } else if (this.form.level == 3) {
                    this.$Modal.warning({
                        title: "Sorry, cannot add",
                        content: "Supports only 2 levels of menu directory"
                    });
                    return;
                } else {
                    type = 0;
                }
                let component = "";
                this.formAdd = {
                    icon: "",
                    type: type,
                    parentId: this.form.id,
                    level: Number(this.form.level) + 1,
                    sortOrder: 0,
                    buttonType: "",
                    status: 0
                };
                if (this.form.level == 0) {
                    this.formAdd.path = "/";
                    this.formAdd.component = "Main";
                } else if (this.form.level == 2) {
                    this.formAdd.path = "None";
                }
                this.menuModalVisible = true;
            },
            changeSelect(v) {
                this.selectCount = v.length;
                this.selectList = v;
            },
            deletePermissionFx() {
                if (this.selectCount <= 0) {
                    this.$Message.warning("Please select the menu to delete");
                    return;
                }
                this.$Modal.confirm({
                    title: "Confirm Delete",
                    content: "Are you sure you want to delete the selected " + this.selectCount + " records?",
                    loading: true,
                    onOk: () => {
                        let ids = "";
                        this.selectList.forEach(function (e) {
                            ids += e.id + ",";
                        });
                        ids = ids.substring(0, ids.length - 1);
                        deletePermission({
                            ids: ids
                        }).then(res => {
                            this.$Modal.remove();
                            if (res.success) {
                                this.$Message.success("Delete successful");
                                this.$store.commit("setAdded", false);
                                util.initRouter(this);
                                this.selectList = [];
                                this.selectCount = 0;
                                this.cancelEdit();
                                this.init();
                            }
                        });
                    }
                });
            }
        },
        mounted() {
            let height = document.documentElement.clientHeight;
            this.maxHeight = Number(height - 260);
            this.init();
        }
    };
    </script>
    