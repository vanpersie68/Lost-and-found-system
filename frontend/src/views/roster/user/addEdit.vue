<template>
    <div class="user-edit">
      <Modal :title="title" v-model="visible" width="90" draggable :mask-closable="type=='0'" ok-text="Submit" @on-ok="submit">
        <Row :gutter="16" justify="center">
          <div class="info-title">
            <span v-show="type!='2'">Login Account {{ form.username }} (Unique Identifier {{ form.id }}) </span>
            <Icon v-show="form.status==-1" type="md-lock" size="18" style="margin-left:10px;cursor:pointer" />
          </div>
        </Row>
        <Form ref="form" :model="form" :rules="formValidate" label-position="top">
          <Row :gutter="16" v-if="type=='2'">
            <Col span="12">
              <FormItem label="Login Account" prop="username" style="width:100%">
                <Input v-model="form.username" autocomplete="off" :maxlength="16" />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="Password" prop="password" style="width:100%">
                <SetPassword v-model="form.password" @on-change="changePass" />
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="16">
            <Col span="12">
              <FormItem label="Username" prop="nickname" style="width:100%">
                <Input v-model="form.nickname" />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="Email" prop="email" style="width:100%">
                <Input v-model="form.email" />
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="16">
            <Col span="12">
              <FormItem label="Mobile Number" prop="mobile" style="width:100%">
                <Input v-model="form.mobile" />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="Gender" style="width:100%">
                <dict dict="sex" v-model="form.sex" transfer clearable placeholder="Select Gender" />
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="16">
            <Col span="12">
              <FormItem label="Department" style="width:100%">
                <department-tree-choose @on-change="handleSelectDepTree" ref="depTree"></department-tree-choose>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="User Type" style="width:100%">
                <Select v-model="form.type" placeholder="Select User Type">
                  <Option :value="0">Regular User</Option>
                  <Option :value="1">Administrator</Option>
                </Select>
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="16">
            <Col span="12">
              <FormItem label="Avatar" style="width:100%">
                <upload-pic-input v-model="form.avatar"></upload-pic-input>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="Role" prop="roleIds" style="width:100%">
                <Select v-model="form.roleIds" multiple>
                  <Option v-for="item in roleList" :value="item.id" :key="item.id" :label="item.name">
                    <span style="margin-right:10px;">{{ item.name }}</span>
                    <span style="color:#ccc;">{{ item.description }}</span>
                  </Option>
                </Select>
              </FormItem>
            </Col>
          </Row>
        </Form>
      </Modal>
    </div>
  </template>
  
  <script>
  import {
    getAllRoleList,
    addUser,
    editUser
  } from "./api.js";
  import {
    validateUsername,
    validateMobile,
    validatePassword
  } from "@/libs/validate";
  import departmentTreeChoose from "@/views/template/department-tree-choose";
  import uploadPicInput from "@/views/template/upload-pic-input";
  import SetPassword from "@/views/template/set-password";
  import dict from "@/views/template/dict";
  export default {
    name: "user",
    components: {
      departmentTreeChoose,
      uploadPicInput,
      SetPassword,
      dict
    },
    props: {
      value: {
        type: Boolean,
        default: false
      },
      data: {
        type: Object
      },
      type: {
        type: String,
        default: "0"
      }
    },
    data() {
      return {
        visible: this.value,
        title: "",
        passColor: "",
        submitLoading: false,
        form: {
          addressArray: []
        },
        formValidate: {
          username: [{
              required: true,
              message: "Please enter login account",
              trigger: "blur"
            },
            {
              validator: validateUsername,
              trigger: "blur"
            }
          ],
          nickname: [{
            required: true,
            message: "Please enter username",
            trigger: "blur"
          }],
          mobile: [{
              required: true,
              message: "Please enter mobile number",
              trigger: "blur"
            },
            {
              validator: validateMobile,
              trigger: "blur"
            }
          ],
          password: [{
              required: true,
              message: "Please enter password",
              trigger: "blur"
            },
            {
              validator: validatePassword,
              trigger: "blur"
            }
          ],
          email: [{
              required: true,
              message: "Please enter email address"
            },
            {
              type: "email",
              message: "Invalid email format"
            }
          ]
        },
        roleList: []
      };
    },
    methods: {
      init() {
        this.getRoleList();
      },
      getRoleList() {
        getAllRoleList().then(res => {
          if (res.success) {
            this.roleList = res.result;
          }
        });
      },
      handleSelectDepTree(v) {
        this.form.departmentId = v;
      },
      changeAddress(v) {
        this.form.address = JSON.stringify(this.form.addressArray);
      },
      changePass(v, grade, strength) {
        this.form.passStrength = strength;
      },
      submit() {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.type == "1") {
              this.submitLoading = true;
              editUser(this.form).then(res => {
                this.submitLoading = false;
                if (res.success) {
                  this.$Message.success("Operation successful");
                  this.$emit("on-submit", true);
                  this.visible = false;
                }
              });
            } else {
              this.submitLoading = true;
              addUser(this.form).then(res => {
                this.submitLoading = false;
                if (res.success) {
                  this.$Message.success("Operation successful");
                  this.$emit("on-submit", true);
                  this.visible = false;
                }
              });
            }
          }
        });
      },
      setCurrentValue(value) {
        if (value === this.visible) {
          return;
        }
        if (this.type == "1") {
          this.title = "Edit";
        } else if (this.type == "2") {
          this.title = "Add";
        } else {
          this.title = "Details";
        }
        this.$refs.form.resetFields();
        if (this.type == "0" || this.type == "1") {
          // Echo data
          let data = this.data;
          // Address
          if (data.address) {
            data.addressArray = JSON.parse(data.address);
          } else {
            data.addressArray = [];
          }
          // Department
          this.$refs.depTree.setData(data.departmentId, data.departmentTitle);
          // Roles
          let selectRolesId = [];
          data.roles.forEach(function(e) {
            selectRolesId.push(e.id);
          });
          data.roleIds = selectRolesId;
          delete data.roles;
          delete data.permissions;
          // Password strength
          if (data.passStrength == "Weak") {
            this.passColor = "#ed3f14";
          } else if (data.passStrength == "Medium") {
            this.passColor = "#faad14";
          } else if (data.passStrength == "Strong") {
            this.passColor = "#52c41a";
          }
          // Echo
          this.form = data;
        } else {
          // Add
          this.$refs.depTree.setData("", "");
          this.form = {
            type: 0,
            sex: "",
            addressArray: []
          };
        }
        this.visible = value;
      }
    },
    watch: {
      value(val) {
        this.setCurrentValue(val);
      },
      visible(value) {
        this.$emit("input", value);
      }
    },
    mounted() {
      this.init();
    }
  };
  </script>
  
  <style lang="less">
  
  </style>
  