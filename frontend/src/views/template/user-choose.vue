<template>
    <div class="user-choose">
      <Button @click="userModalVisible=true" :icon="icon">{{ text }}</Button>
      <span @click="clearSelectData" class="clear">Clear Selection</span>
      <Collapse simple class="collapse">
        <Panel name="1">
          Selected
          <span class="select-count">{{ selectUsers.length }}</span> people
          <p slot="content">
            <Tag v-for="(item, i) in selectUsers" :key="i" :name="item.id" color="default" closable @on-close="handleCancelUser">
              <Tooltip placement="top" :content="item.username">{{ item.nickname }}</Tooltip>
            </Tag>
          </p>
        </Panel>
      </Collapse>
      <Drawer title="Select Users" closable v-model="userModalVisible" width="800" draggable>
        <Form ref="searchUserForm" :model="searchUserForm" inline :label-width="55">
          <Form-item label="Username" prop="nickname">
            <Input type="text" v-model="searchUserForm.nickname" clearable placeholder="Enter username" style="width: 200px" />
          </Form-item>
          <Form-item label="Department" prop="department">
            <department-choose @on-change="handleSelectDep" style="width: 200px" ref="dep"></department-choose>
          </Form-item>
          <Form-item style="margin-left:-35px;" class="br">
            <Button @click="handleSearchUser" type="primary" icon="ios-search">Search</Button>
            <Button @click="handleResetUser">Reset</Button>
          </Form-item>
        </Form>
        <Alert show-icon>
          Selected
          <span class="select-count">{{ selectUsers.length }}</span> items
          <a style="margin-left: 10px;" @click="clearSelectData">Clear Selection</a>
        </Alert>
        <Table :loading="userLoading" border :columns="userColumns" :data="userData" style="margin: 2vh 0;"></Table>
        <Row type="flex" justify="end">
          <Page :current="searchUserForm.pageNumber" :total="totalUser" :page-size="searchUserForm.pageSize" @on-change="changeUserPage" @on-page-size-change="changeUserPageSize" :page-size-opts="[10,20,50]" size="small" show-total show-elevator show-sizer transfer></Page>
        </Row>
      </Drawer>
    </div>
  </template>
  
  <script>
  import {
    getUserListData
  } from "@/api/index";
  import departmentChoose from "./department-choose";
  
  export default {
    name: "userChoose",
    components: {
      departmentChoose
    },
    props: {
      text: {
        type: String,
        default: "Select Users"
      },
      icon: {
        type: String,
        default: "md-person-add"
      }
    },
    data() {
      return {
        userLoading: true,
        userModalVisible: false,
        selectUsers: [],
        searchUserForm: {
          id: "",
          nickname: "",
          type: "",
          status: "",
          pageNumber: 1, // Current page number
          pageSize: 10, // Page size
          sort: "createTime", // Default sorting field
          order: "desc" // Default sorting order
        },
        userColumns: [{
            type: "index",
            width: 60,
            align: "center"
          },
          {
            title: "Username",
            key: "nickname",
            minWidth: 130,
            sortable: true
          },
          {
            title: "Login Account",
            key: "username",
            minWidth: 130,
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
            width: 120
          },
          {
            title: "Mobile",
            key: "mobile",
            width: 125,
            sortable: true
          },
          {
            title: "Email",
            key: "email",
            width: 180,
            sortable: true
          },
          {
            title: "Gender",
            key: "sex",
            width: 70,
            align: "center"
          },
          {
            title: "Creation Time",
            key: "createTime",
            sortable: true,
            sortType: "desc",
            width: 170
          },
          {
            title: "Action",
            key: "action",
            width: 130,
            align: "center",
            fixed: "right",
            render: (h, params) => {
              return h("div", [
                h(
                  "Button", {
                    props: {
                      type: "info",
                      size: "small"
                    },
                    on: {
                      click: () => {
                        this.chooseUser(params.row);
                      }
                    }
                  },
                  "Add User"
                )
              ]);
            }
          }
        ],
        userData: [],
        totalUser: 0
      };
    },
    methods: {
      handleSelectDep(v) {
        this.searchUserForm.departmentId = v;
      },
      changeUserPage(v) {
        this.searchUserForm.pageNumber = v;
        this.getUserDataList();
      },
      changeUserPageSize(v) {
        this.searchUserForm.pageSize = v;
        this.getUserDataList();
      },
      getUserDataList() {
        this.userLoading = true;
        getUserListData(this.searchUserForm).then(res => {
          this.userLoading = false;
          if (res.success) {
            this.userData = res.result.records;
            this.totalUser = res.result.total;
          }
        });
      },
      handleSearchUser() {
        this.searchUserForm.pageNumber = 1;
        this.searchUserForm.pageSize = 10;
        this.getUserDataList();
      },
      handleResetUser() {
        this.$refs.searchUserForm.resetFields();
        this.searchUserForm.pageNumber = 1;
        this.searchUserForm.pageSize = 10;
        this.$refs.dep.clearSelect();
        this.searchUserForm.departmentId = "";
        // Reload data
        this.getUserDataList();
      },
      setData(v) {
        this.selectUsers = v;
        this.$emit("on-change", this.selectUsers);
      },
      chooseUser(v) {
        // Check for duplicates
        let that = this;
        let flag = true;
        this.selectUsers.forEach(e => {
          if (v.id == e.id) {
            that.$Message.warning("Already added, please do not select again");
            flag = false;
          }
        });
        if (flag) {
          let u = {
            id: v.id,
            username: v.username,
            nickname: v.nickname
          };
          this.selectUsers.push(u);
          this.$emit("on-change", this.selectUsers);
          this.$Message.success(`Added user ${v.nickname} successfully`);
        }
      },
      clearSelectData() {
        this.selectUsers = [];
        this.$emit("on-change", this.selectUsers);
      },
      handleCancelUser(e, id) {
        // Remove the selected user
        this.selectUsers = this.selectUsers.filter(e => {
          return e.id != id;
        });
        this.$emit("on-change", this.selectUsers);
        this.$Message.success("Deleted selected user successfully");
      }
    },
    mounted() {
      this.getUserDataList();
    }
  };
  </script>
  
  <style lang="less">
  .user-choose {
    .clear {
      font-size: 12px;
      margin-left: 15px;
      color: #40a9ff;
      cursor: pointer;
    }
  
    .collapse {
      font-size: 12px;
      margin-top: 15px;
      width: 500px;
    }
  
    .select-count {
      font-weight: 600;
      color: #40a9ff;
    }
  }
  </style>
  