<template>
    <div class="search">
      <Card>
        <Row :gutter="16">
          <Form ref="searchForm" :model="searchForm" inline :label-width="0">
            <Form-item label="">
              <Input type="text" v-model="searchForm.name" placeholder="Role Name" clearable style="width: 200px" />
            </Form-item>
            <Form-item label="">
              <Input type="text" v-model="searchForm.description" placeholder="Role Description" clearable style="width: 200px" />
            </Form-item>
            <Form-item label="">
              <Button type="success" @click="init" ghost shape="circle" size="small" icon="md-refresh">Search</Button>
              <Divider type="vertical" />
              <Button @click="addRoleFx" type="primary" ghost shape="circle" size="small" icon="md-add" :disabled="!$route.meta.permTypes.includes('delete')">Add Role</Button>
            </Form-item>
          </Form>
        </Row>
        <Divider dashed />
        <Row :gutter="16">
          <Table :loading="loading" border stripe size="small" :columns="columns" :data="data" ref="table" sortable="custom" @on-sort-change="changeSort"></Table>
        </Row>
        <Row type="flex" justify="end" class="page">
          <Page :current="searchForm.pageNumber" :total="total" :page-size="searchForm.pageSize" @on-change="changePage" @on-page-size-change="changePageSize" :page-size-opts="[15,20,50]" size="small" show-total show-elevator show-sizer></Page>
        </Row>
      </Card>
  
      <!-- Edit -->
      <Modal :title="modalTitle" v-model="roleModalVisible" :mask="false" draggable ok-text="Save Role Data" @on-ok="submitRole" @on-cancel="cancelRole">
        <Form ref="roleForm" :model="roleForm" :label-width="80" :rules="roleFormValidate">
          <Row :gutter="16">
            <Col span="24">
              <FormItem label="Role Name" prop="name">
                <Input v-model="roleForm.name" placeholder="Role name, recommended to start with ROLE_" />
              </FormItem>
              <FormItem label="Remark" prop="description">
                <Input v-model="roleForm.description" />
              </FormItem>
            </Col>
          </Row>
        </Form>
      </Modal>
      <!-- Menu Permissions -->
      <Modal :title="modalTitle" v-model="permModalVisible" :mask="false" draggable ok-text="Save Menu Permissions" @on-ok="submitPermEdit" @on-cancel="cancelPermEdit">
        <Row :gutter="16">
          <Col span="24" style="max-height:500px;overflow-y: scroll;">
            <Tree ref="tree" :data="permData" show-checkbox :render="renderContent" :check-strictly="true"></Tree>
            <Spin size="large" fix v-if="treeLoading"></Spin>
          </Col>
        </Row>
      </Modal>
    </div>
  </template>
  
  <script>
  import {
    getRoleList,
    getAllPermissionList,
    addRole,
    editRole,
    deleteRole,
    setDefaultRole,
    editRolePerm
  } from "./api.js";
  import util from "@/libs/util.js";
  export default {
    name: "role",
    data() {
      return {
        loading: true,
        treeLoading: true,
        submitPermLoading: false,
        modalType: 0,
        roleModalVisible: false,
        permModalVisible: false,
        modalTitle: "",
        roleForm: {
          name: "",
          description: ""
        },
        roleFormValidate: {
          name: [{
            required: true,
            message: "Role name cannot be empty",
            trigger: "blur"
          }]
        },
        submitLoading: false,
        columns: [{
            type: "index",
            width: 60,
            align: "center"
          },
          {
            title: "Role Name",
            key: "name",
            minWidth: 150,
            sortable: true
          },
          {
            title: "Remark",
            key: "description",
            minWidth: 260,
            sortable: true
          },
          {
            title: "Creation Time",
            key: "createTime",
            minWidth: 170,
            sortable: true,
            sortType: "desc"
          },
          {
            title: "Creator",
            key: "createBy",
            minWidth: 170,
            sortable: true,
            sortType: "desc"
          },
          {
            title: "Update Time",
            key: "updateTime",
            minWidth: 170,
            sortable: true
          },
          {
            title: "Last Updated By",
            key: "updateBy",
            minWidth: 170,
            sortable: true
          },
          {
            title: "Action",
            key: "action",
            align: "center",
            fixed: "right",
            width: 380,
            render: (h, params) => {
              var that = this;
              return h("div", [
                h(
                  "Button", {
                    props: {
                      type: "success",
                      size: "small",
                      ghost: true,
                      shape: "circle",
                      icon: "ios-brush",
                      disabled: !(that.$route.meta.permTypes && that.$route.meta.permTypes.includes("enable"))
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.editPerm(params.row);
                      }
                    }
                  },
                  "Menu Permissions"
                ),
                h(
                  "Button", {
                    props: {
                      type: params.row.defaultRole ? "primary" : "info",
                      size: "small",
                      ghost: true,
                      shape: "circle",
                      icon: "ios-browsers"
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        if (params.row.defaultRole) {
                          this.cancelDefault(params.row);
                        } else {
                          this.setDefault(params.row);
                        }
                      }
                    }
                  },
                  params.row.defaultRole ? "Cancel Default" : "Set Default"
                ),
                h(
                  "Button", {
                    props: {
                      type: "warning",
                      size: "small",
                      ghost: true,
                      shape: "circle",
                      icon: "md-color-wand",
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
                      ghost: true,
                      shape: "circle",
                      icon: "ios-undo",
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
        data: [],
        total: 0,
        permData: [],
        editRolePermId: "",
        searchForm: {
          pageNumber: 1,
          pageSize: 15,
          sort: "createTime",
          order: "desc",
        },
      };
    },
    methods: {
      init() {
        this.getRoleList();
        this.getPermList();
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
        return h(
          "span", {
            style: {
              display: "inline-block",
              cursor: "pointer"
            },
            on: {
              click: () => {
                data.checked = !data.checked;
              }
            }
          },
          [
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
          ]
        );
      },
      changePage(v) {
        this.searchForm.pageNumber = v;
        this.getRoleList();
      },
      changePageSize(v) {
        this.searchForm.pageSize = v;
        this.getRoleList();
      },
      changeSort(e) {
        this.searchForm.sort = e.key;
        this.searchForm.order = e.order;
        if (e.order === "normal") {
          this.searchForm.order = "";
        }
        this.getRoleList();
      },
      getRoleList() {
        this.loading = true;
        getRoleList(this.searchForm).then(res => {
          this.loading = false;
          if (res.success) {
            this.data = res.result.records;
            this.total = res.result.total;
          }
        });
      },
      getPermList() {
        this.treeLoading = true;
        getAllPermissionList().then(res => {
          if (res.success) {
            this.deleteDisableNode(res.result);
            this.permData = res.result;
            this.treeLoading = false;
          }
          this.treeLoading = false;
        });
      },
      deleteDisableNode(permData) {
        let that = this;
        permData.forEach(function (e) {
          if (e.status == -1) {
            e.title = "[Disabled] " + e.title;
            e.disabled = true;
          }
          if (e.children && e.children.length > 0) {
            that.deleteDisableNode(e.children);
          }
        });
      },
      cancelRole() {
        this.roleModalVisible = false;
      },
      submitRole() {
        this.$refs.roleForm.validate(valid => {
          if (valid) {
            if (this.modalType == 0) {
              this.submitLoading = true;
              addRole(this.roleForm).then(res => {
                this.submitLoading = false;
                if (res.success) {
                  this.$Message.success("Operation succeeded");
                  this.getRoleList();
                  this.roleModalVisible = false;
                }
              });
            } else {
              this.submitLoading = true;
              delete this.roleForm.permissions;
              editRole(this.roleForm).then(res => {
                this.submitLoading = false;
                if (res.success) {
                  this.$Message.success("Operation succeeded");
                  this.getRoleList();
                  this.roleModalVisible = false;
                }
              });
            }
          }
        });
      },
      addRoleFx() {
        this.modalType = 0;
        this.modalTitle = "Add Role";
        this.$refs.roleForm.resetFields();
        delete this.roleForm.id;
        this.roleModalVisible = true;
      },
      edit(v) {
        this.modalType = 1;
        this.modalTitle = "Edit Role";
        this.$refs.roleForm.resetFields();
        for (let attr in v) {
          if (v[attr] == null) {
            v[attr] = "";
          }
        }
        let str = JSON.stringify(v);
        let roleInfo = JSON.parse(str);
        this.roleForm = roleInfo;
        this.roleModalVisible = true;
      },
      remove(v) {
        this.$Modal.confirm({
          title: "Confirm Deletion",
          content: "Are you sure you want to delete the role " + v.name + "?",
          loading: true,
          onOk: () => {
            deleteRole({
              ids: v.id
            }).then(res => {
              this.$Modal.remove();
              if (res.success) {
                this.$Message.success("Deleted successfully");
                this.getRoleList();
              }
            });
          }
        });
      },
      setDefault(v) {
        this.$Modal.confirm({
          title: "Confirm Set Default",
          content: "Are you sure you want to set " + v.name + " as the default role for registered users?",
          loading: true,
          onOk: () => {
            let params = {
              id: v.id,
              isDefault: true
            };
            setDefaultRole(params).then(res => {
              this.$Modal.remove();
              if (res.success) {
                this.$Message.success("Operation succeeded");
                this.getRoleList();
              }
            });
          }
        });
      },
      cancelDefault(v) {
        this.$Modal.confirm({
          title: "Confirm Cancel Default",
          content: "Are you sure you want to cancel " + v.name + " as the default role?",
          loading: true,
          onOk: () => {
            let params = {
              id: v.id,
              isDefault: false
            };
            setDefaultRole(params).then(res => {
              this.$Modal.remove();
              if (res.success) {
                this.$Message.success("Operation succeeded");
                this.getRoleList();
              }
            });
          }
        });
      },
      editPerm(v) {
        this.editRolePermId = v.id;
        this.modalTitle = v.name + " Menu Permissions Modification";
        let rolePerms = v.permissions;
        if (this.treeLoading) {
          this.$Message.warning("Menu permissions data is loading. Please click to view later.");
          return;
        }
        this.checkPermTree(this.permData, rolePerms);
        this.permModalVisible = true;
      },
      checkPermTree(permData, rolePerms) {
        let that = this;
        permData.forEach(function (p) {
          if (that.hasPerm(p, rolePerms) && p.status != -1) {
            p.checked = true;
          } else {
            p.checked = false;
          }
          if (p.children && p.children.length > 0) {
            that.checkPermTree(p.children, rolePerms);
          }
        });
      },
      hasPerm(p, rolePerms) {
        let flag = false;
        for (let i = 0; i < rolePerms.length; i++) {
          if (p.id == rolePerms[i].permissionId) {
            flag = true;
            break;
          }
        }
        if (flag) {
          return true;
        }
        return false;
      },
      selectedTreeAll(permData, select) {
        let that = this;
        permData.forEach(function (e) {
          e.checked = select;
          if (e.children && e.children.length > 0) {
            that.selectedTreeAll(e.children, select);
          }
        });
      },
      submitPermEdit() {
        this.submitPermLoading = true;
        let permIds = "";
        let selectedNodes = this.$refs.tree.getCheckedNodes();
        selectedNodes.forEach(function (e) {
          permIds += e.id + ",";
        });
        permIds = permIds.substring(0, permIds.length - 1);
        editRolePerm({
          roleId: this.editRolePermId,
          permIds: permIds
        }).then(res => {
          this.submitPermLoading = false;
          if (res.success) {
            this.$Message.success("Operation succeeded");
            this.$store.commit("setAdded", false);
            util.initRouter(this);
            this.getRoleList();
            this.permModalVisible = false;
          }
        });
      },
      cancelPermEdit() {
        this.permModalVisible = false;
      }
    },
    mounted() {
      this.init();
    }
  };
  </script>
  
  <style lang="less">
  @import "../../../styles/table-common.less";
  
  .permModal {
    .ivu-modal-body {
      max-height: 560px;
      overflow: auto;
    }
  }
  
  .depModal {
    .ivu-modal-body {
      max-height: 500px;
      overflow: auto;
    }
  }
  </style>
  