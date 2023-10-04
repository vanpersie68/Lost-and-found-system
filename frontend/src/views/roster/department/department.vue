<template>
    <div class="search">
      <Card>
        <Row class="operation">
          <Button
            @click="add"
            type="primary"
            icon="md-add"
            ghost
            shape="circle"
            size="small"
            :disabled="!$route.meta.permTypes.includes('add')"
          >
            Add
          </Button>
          <Button
            @click="delAll"
            icon="md-trash"
            type="error"
            ghost
            shape="circle"
            size="small"
            :disabled="!$route.meta.permTypes.includes('delete')"
          >
            Delete
          </Button>
          <Button
            @click="getParentList"
            icon="md-refresh"
            type="info"
            ghost
            shape="circle"
            size="small"
          >
            Refresh
          </Button>
          <Button
            @click="excelData"
            type="success"
            icon="md-paper-plane"
            ghost
            shape="circle"
            size="small"
          >
            Export Users
          </Button>
          <i-switch v-model="strict" size="large" style="margin-left: 5px">
            <span slot="open">Cascade</span>
            <span slot="close">Single</span>
          </i-switch>
        </Row>
        <Row :gutter="16">
          <Col span="8">
            <Alert show-icon type="success" v-show="form.id">
              Current Selection
              <span class="select-title">{{ editTitle }}</span>
              <a class="select-clear" @click="cancelEdit"> Cancel Selection</a>
            </Alert>
            <Input
              v-model="searchKey"
              suffix="ios-search"
              @on-change="search"
              placeholder="Search by Department Name"
              clearable
            />
            <div class="tree-bar" :style="{ maxHeight: maxHeight }">
              <Tree
                ref="tree"
                :data="data"
                :load-data="loadData"
                show-checkbox
                @on-check-change="changeSelect"
                @on-select-change="selectTree"
                :check-strictly="!strict"
              ></Tree>
              <Spin size="large" fix v-if="loading"></Spin>
            </div>
          </Col>
          <Col span="16">
            <Form
              ref="form"
              :model="form"
              :label-width="100"
              :rules="formValidate"
            >
              <Row :gutter="16">
                <Col span="12">
                  <FormItem label="Parent Department" prop="parentTitle">
                    <div style="display: flex;">
                      <Input
                        v-model="form.parentTitle"
                        readonly
                        style="margin-right: 10px;"
                      />
                      <Poptip
                        transfer
                        trigger="click"
                        placement="right-start"
                        title="Select Parent Department"
                        width="250"
                      >
                        <Button icon="md-list">Select Department</Button>
                        <div slot="content" style="position: relative; min-height: 5vh">
                          <Tree
                            :data="dataEdit"
                            :load-data="loadData"
                            @on-select-change="selectTreeEdit"
                          ></Tree>
                          <Spin size="large" fix v-if="loadingEdit"></Spin>
                        </div>
                      </Poptip>
                    </div>
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem label="Department Name" prop="title">
                    <Input v-model="form.title" />
                  </FormItem>
                </Col>
              </Row>
              <Row :gutter="16">
                <Col span="12">
                  <FormItem label="Department Head" prop="mainHeader">
                    <Select
                      :loading="userLoading"
                      not-found-text="No users available in this department"
                      v-model="form.mainHeader"
                      multiple
                      filterable
                      placeholder="Select Department Head"
                    >
                      <Option
                        v-for="item in users"
                        :value="item.id"
                        :key="item.id"
                        :label="item.nickname"
                      >
                        <span style="margin-right: 10px;">{{ item.nickname }}</span>
                        <span style="color: #ccc;">{{ item.username }}</span>
                      </Option>
                    </Select>
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem label="Department Leader" prop="viceHeader">
                    <Select
                      :loading="userLoading"
                      not-found-text="No users available in this department"
                      v-model="form.viceHeader"
                      multiple
                      filterable
                      placeholder="Select Department Leader"
                    >
                      <Option
                        v-for="item in users"
                        :value="item.id"
                        :key="item.id"
                        :label="item.nickname"
                      >
                        <span style="margin-right: 10px;">{{ item.nickname }}</span>
                        <span style="color: #ccc;">{{ item.username }}</span>
                      </Option>
                    </Select>
                  </FormItem>
                </Col>
              </Row>
              <Row :gutter="16">
                <Col span="8">
                  <FormItem label="Sort Order" prop="sortOrder">
                    <Tooltip
                      trigger="hover"
                      placement="right"
                      content="Smaller values indicate higher priority, supports decimals"
                    >
                      <InputNumber :max="1000" :min="0" v-model="form.sortOrder"></InputNumber>
                    </Tooltip>
                  </FormItem>
                </Col>
                <Col span="8">
                  <FormItem label="Is Enabled" prop="status">
                    <i-switch
                      size="large"
                      v-model="form.status"
                      :true-value="0"
                      :false-value="-1"
                    >
                      <span slot="open">Enabled</span>
                      <span slot="close">Disabled</span>
                    </i-switch>
                  </FormItem>
                </Col>
                <Col span="8">
                  <Form-item class="br">
                    <Button
                      @click="submitEdit"
                      :loading="submitLoading"
                      type="success"
                      ghost
                      shape="circle"
                      icon="ios-create-outline"
                      size="small"
                      :disabled="!$route.meta.permTypes.includes('edit')"
                    >
                      Save
                    </Button>
                  </Form-item>
                </Col>
              </Row>
            </Form>
          </Col>
        </Row>
      </Card>
      <Divider dashed>Users in the Department</Divider>
      <Card>
        <Row>
          <Table
            :loading="userLoading"
            border
            :columns="userColumns"
            :data="userData"
            ref="table"
            sortable="custom"
            @on-sort-change="changeSort"
            @on-selection-change="changeSelect"
            @on-row-click="rowClick"
            :row-class-name="rowClassName"
          ></Table>
        </Row>
        <Row type="flex" justify="end" class="page">
          <Page
            :current="searchForm.pageNumber"
            :total="userTotal"
            :page-size="searchForm.pageSize"
            @on-change="changePage"
            @on-page-size-change="changePageSize"
            :page-size-opts="[10, 20, 50]"
            size="small"
            show-total
            show-elevator
            show-sizer
          ></Page>
        </Row>
      </Card>
  
      <Modal
        :title="modalTitle"
        v-model="addOrEditDepartmentModal"
        :mask-closable="false"
        :width="500"
        on-text="Submit"
        @on-ok="submitAdd"
      >
        <Form ref="formAdd" :model="formAdd" :label-width="85" :rules="formValidate">
          <Row :gutter="16" v-show="showParent">
            <Col span="24">
              <FormItem label="Parent Department:">{{ form.title }}</FormItem>
            </Col>
          </Row>
          <Row :gutter="16">
            <Col span="24">
              <FormItem label="Department Name" prop="title">
                <Input v-model="formAdd.title" />
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="16">
            <Col span="12">
              <FormItem label="Sort Order" prop="sortOrder">
                <Tooltip
                  trigger="hover"
                  placement="right"
                  content="Smaller values indicate higher priority, supports decimals"
                >
                  <InputNumber :max="1000" :min="0" v-model="formAdd.sortOrder"></InputNumber>
                </Tooltip>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="Is Enabled" prop="status">
                <i-switch
                  size="large"
                  v-model="formAdd.status"
                  :true-value="0"
                  :false-value="-1"
                >
                  <span slot="open">Enabled</span>
                  <span slot="close">Disabled</span>
                </i-switch>
              </FormItem>
            </Col>
          </Row>
        </Form>
      </Modal>
    </div>
  </template>
  
  <script>
  import {
    initDepartment,
    addDepartment,
    editDepartment,
    deleteDepartment,
    searchDepartment,
    getUserByDepartmentId,
    getMyUserListData
  } from "./api.js";
  
  export default {
    name: "department-manage",
    data() {
      return {
        userLoading: true,
        searchForm: {
          pageNumber: 1,
          pageSize: 10,
          sort: "createTime",
          order: "desc",
          departmentId: ""
        },
        userData: [],
        userTotal: 0,
        selectList: [],
        selectCount: 0,
        selectRow: {},
        loading: true,
        maxHeight: "500px",
        strict: true,
        userLoading: false,
        loadingEdit: true,
        addOrEditDepartmentModal: false,
        selectList: [],
        selectCount: 0,
        showParent: false,
        modalTitle: "",
        editTitle: "",
        searchKey: "",
        form: {
          id: "",
          title: "",
          parentId: "",
          parentTitle: "",
          sortOrder: 0,
          status: 0
        },
        formAdd: {},
        formValidate: {
          title: [
            {
              required: true,
              message: "Name cannot be empty",
              trigger: "blur"
            }
          ],
          sortOrder: [
            {
              required: true,
              type: "number",
              message: "Sort order cannot be empty",
              trigger: "blur"
            }
          ]
        },
        submitLoading: false,
        data: [],
        dataEdit: [],
        users: [],
        userColumns: [
          {
            type: "selection",
            width: 60,
            align: "center",
            fixed: "left"
          },
          {
            type: "index",
            width: 60,
            align: "center",
            fixed: "left"
          },
          {
            title: "Username",
            key: "nickname",
            minWidth: 125,
            sortable: true,
            fixed: "left"
          },
          {
            title: "Login Account",
            key: "username",
            minWidth: 125,
            sortable: true
          },
          {
            title: "Avatar",
            key: "avatar",
            width: 80,
            align: "center",
            render: (h, params) => {
              return h("Avatar", {
                props: {
                  src: params.row.avatar
                }
              });
            }
          },
          {
            title: "Department",
            key: "departmentTitle",
            minWidth: 120
          },
          {
            title: "Mobile",
            key: "mobile",
            minWidth: 125,
            sortable: true
          },
          {
            title: "Email",
            key: "email",
            minWidth: 180,
            sortable: true
          },
          {
            title: "Gender",
            key: "sex",
            width: 70,
            align: "center"
          },
          {
            title: "Type",
            key: "type",
            align: "center",
            width: 100,
            render: (h, params) => {
              if (params.row.type == 1) {
                return h("div", [
                  h(
                    "Tag",
                    {
                      props: {
                        color: "magenta",
                        size: "medium"
                      }
                    },
                    "Admin"
                  )
                ]);
              } else {
                return h("div", [
                  h(
                    "Tag",
                    {
                      props: {
                        color: "blue",
                        size: "default"
                      }
                    },
                    "User"
                  )
                ]);
              }
            }
          },
          {
            title: "Status",
            key: "status",
            align: "center",
            width: 110,
            render: (h, params) => {
              return h("div", [
                h(
                  "Tag",
                  {
                    props: {
                      color: params.row.status == 0 ? "green" : "red",
                      size: "medium"
                    }
                  },
                  params.row.status == 0 ? "Enabled" : "Disabled"
                )
              ]);
            }
          }
        ]
      };
    },
    methods: {
      init() {
        this.getParentList();
        this.getParentListEdit();
        this.getUserDataList();
      },
      getUserDataList() {
        var that = this;
        that.userLoading = true;
        getMyUserListData(that.searchForm).then(res => {
          that.userLoading = false;
          if (res.success) {
            that.userData = res.result.records;
            that.userTotal = res.result.total;
          }
        });
      },
      getParentList() {
        this.loading = true;
        initDepartment().then(res => {
          this.loading = false;
          if (res.success) {
            res.result.forEach(function(e) {
              if (e.isParent) {
                e.loading = false;
                e.children = [];
                e._loading = false;
              }
            });
            this.data = res.result;
          }
        });
      },
      getParentListEdit() {
        this.loadingEdit = true;
        initDepartment().then(res => {
          this.loadingEdit = false;
          if (res.success) {
            res.result.forEach(function(e) {
              if (e.isParent) {
                e.loading = false;
                e.children = [];
              }
            });
            let first = {
              id: "0",
              title: "First-level Department"
            };
            res.result.unshift(first);
            this.dataEdit = res.result;
          }
        });
      },
      loadData(item, callback) {
        initDepartment({
          parentId: item.id
        }).then(res => {
          if (res.success) {
            res.result.forEach(function(e) {
              if (e.isParent) {
                e.loading = false;
                e.children = [];
                e._loading = false;
              }
            });
            callback(res.result);
          }
        });
      },
      search() {
        if (this.searchKey) {
          this.loading = true;
          searchDepartment({
            title: this.searchKey
          }).then(res => {
            this.loading = false;
            if (res.success) {
              this.data = res.result;
            }
          });
        } else {
          this.getParentList();
        }
      },
      excelData() {
        this.$refs.table.exportCsv({
          filename: "Department User Export Results"
        });
      },
      selectTree(v) {
        if (v.length > 0) {
          for (let attr in v[0]) {
            if (v[0][attr] == null) {
              v[0][attr] = "";
            }
          }
          let str = JSON.stringify(v[0]);
          let data = JSON.parse(str);
          this.editTitle = data.title;
          this.userLoading = true;
          getUserByDepartmentId({
            departmentId: data.id
          }).then(res => {
            this.userLoading = false;
            if (res.success) {
              this.users = res.result;
              this.form = data;
            }
          });
          this.searchForm.departmentId = v[0].id;
          this.getUserDataList();
        } else {
          this.cancelEdit();
          this.searchForm.departmentId = "";
          this.getUserDataList();
        }
      },
      cancelEdit() {
        let data = this.$refs.tree.getSelectedNodes()[0];
        if (data) {
          data.selected = false;
        }
        this.$refs.form.resetFields();
        delete this.form.id;
        this.editTitle = "";
      },
      selectTreeEdit(v) {
        if (v.length > 0) {
          for (let attr in v[0]) {
            if (v[0][attr] == null) {
              v[0][attr] = "";
            }
          }
          let str = JSON.stringify(v[0]);
          let data = JSON.parse(str);
          this.form.parentId = data.id;
          this.form.parentTitle = data.title;
        }
      },
      handleReset() {
        this.$refs.form.resetFields();
        this.form.status = 0;
      },
      showSelect(e) {
        this.selectList = e;
        this.selectCount = e.length;
      },
      clearSelectAll() {
        this.$refs.table.selectAll(false);
      },
      submitEdit() {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (!this.form.id) {
              this.$Message.warning("Please select the department to edit first");
              return;
            }
            this.submitLoading = true;
            editDepartment(this.form).then(res => {
              this.submitLoading = false;
              if (res.success) {
                this.$Message.success("Edit successful");
                this.init();
                this.addOrEditDepartmentModal = false;
              }
            });
          }
        });
      },
      submitAdd() {
        this.$refs.formAdd.validate(valid => {
          if (valid) {
            this.submitLoading = true;
            addDepartment(this.formAdd).then(res => {
              this.submitLoading = false;
              if (res.success) {
                this.$Message.success("Add successful");
                this.init();
                this.addOrEditDepartmentModal = false;
              }
            });
          }
        });
      },
      delAll() {
        this.selectList.length > 0
          ? this.$Modal.confirm({
              title: "Warning",
              content: "Delete selected users?",
              onOk: () => {
                this.submitLoading = true;
                deleteDepartment({
                  departmentIds: this.selectList.map(Number)
                }).then(res => {
                  this.submitLoading = false;
                  if (res.success) {
                    this.$Message.success("Delete successful");
                    this.init();
                    this.clearSelectAll();
                  }
                });
              }
            })
          : this.$Message.warning("Please select the department to delete first");
      },
      rowClick(e) {
        this.selectRow = e;
        this.addOrEditDepartmentModal = true;
        this.modalTitle = "Edit Department";
        this.form = Object.assign({}, e);
        this.form.status = e.status == "Enabled" ? 0 : -1;
        this.form.parentTitle = this.getParentTitle(e.parentId);
      },
      getParentTitle(id) {
        let title = "";
        if (id) {
          this.dataEdit.forEach(item => {
            if (item.id == id) {
              title = item.title;
            }
          });
        }
        return title;
      },
      add() {
        if (this.selectRow.title) {
          this.addOrEditDepartmentModal = true;
          this.modalTitle = "Add Department";
          this.formAdd = {
            parentId: this.selectRow.id,
            title: "",
            sortOrder: 0,
            status: 0
          };
          this.showParent = true;
        } else {
          this.$Message.warning("Please select the parent department first");
        }
      },
      changePage(e) {
        this.searchForm.pageNumber = e;
        this.getUserDataList();
      },
      changePageSize(e) {
        this.searchForm.pageNumber = 1;
        this.searchForm.pageSize = e;
        this.getUserDataList();
      },
      changeSort(e) {
        this.searchForm.sort = e.prop;
        this.searchForm.order = e.order == "ascending" ? "asc" : "desc";
        this.getUserDataList();
      },
      rowClassName(row, index) {
        if (row.status == "Enabled") {
          return "";
        } else {
          return "warning-row";
        }
      }
    },
    created() {
      this.init();
    }
  };
  </script>
  
  <style scoped>
  .search {
    padding: 20px;
  }
  .operation {
    margin-bottom: 10px;
    display: flex;
    justify-content: space-between;
  }
  .select-title {
    font-weight: bold;
  }
  .select-clear {
    color: #1890ff;
    cursor: pointer;
  }
  .tree-bar {
    overflow-y: auto;
  }
  .page {
    margin-top: 10px;
  }
  </style>
  