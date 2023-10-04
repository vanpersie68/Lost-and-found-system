<template>
    <div>
      <Card class="change-pass">
        <p slot="title">
          <Icon type="key"></Icon> Change Password
        </p>
        <div>
          <Form ref="editPasswordForm" :model="editPasswordForm" :label-width="100" label-position="right" :rules="passwordValidate" style="width:450px">
            <FormItem label="Current Password" prop="oldPass">
              <Input type="password" v-model="editPasswordForm.oldPass" placeholder="Enter your current password"></Input>
            </FormItem>
            <FormItem label="New Password" prop="newPass">
              <SetPassword v-model="editPasswordForm.newPass" @on-change="changeInputPass" />
            </FormItem>
            <FormItem label="Confirm New Password" prop="rePass">
              <Input type="password" v-model="editPasswordForm.rePass" placeholder="Please enter the new password again"></Input>
            </FormItem>
            <FormItem>
              <Button type="primary" style="width: 100px; margin-right: 5px" :loading="savePassLoading" @click="saveEditPass">Save</Button>
              <Button @click="cancelEditPass">Cancel</Button>
            </FormItem>
          </Form>
        </div>
      </Card>
    </div>
  </template>
  
  <script>
  import {
    changePass
  } from "./api.js";
  import SetPassword from "@/views/template/set-password";
  export default {
    name: "change_pass",
    components: {
      SetPassword
    },
    data() {
      const validateRePassword = (rule, value, callback) => {
        if (value !== this.editPasswordForm.newPass) {
          callback(new Error("The two passwords do not match"));
        } else {
          callback();
        }
      };
      return {
        savePassLoading: false,
        editPasswordForm: {
          oldPass: "",
          newPass: "",
          rePass: ""
        },
        strength: "",
        passwordValidate: {
          oldPass: [{
            required: true,
            message: "Please enter the current password",
            trigger: "blur"
          }],
          newPass: [{
              required: true,
              message: "Please enter the new password",
              trigger: "blur"
            },
            {
              min: 6,
              message: "Please enter at least 6 characters",
              trigger: "blur"
            },
            {
              max: 32,
              message: "Enter at most 32 characters",
              trigger: "blur"
            }
          ],
          rePass: [{
              required: true,
              message: "Please enter the new password again",
              trigger: "blur"
            },
            {
              validator: validateRePassword,
              trigger: "blur"
            }
          ]
        }
      };
    },
    methods: {
      changeInputPass(v, grade, strength) {
        this.strength = strength;
      },
      saveEditPass() {
        let params = {
          password: this.editPasswordForm.oldPass,
          newPass: this.editPasswordForm.newPass,
          passStrength: this.strength
        };
        this.$refs["editPasswordForm"].validate(valid => {
          if (valid) {
            this.savePassLoading = true;
            changePass(params).then(res => {
              this.savePassLoading = false;
              if (res.success) {
                this.$Modal.success({
                  title: "Password Changed Successfully",
                  content: "Password changed successfully. Please log in again.",
                  onOk: () => {
                    this.$store.commit("logout", this);
                    this.$store.commit("clearOpenedSubmenu");
                    this.$router.push({
                      name: "login"
                    });
                  }
                });
              }
            });
          }
        });
      },
      cancelEditPass() {
        this.$store.commit("removeTag", "password");
        localStorage.pageOpenedList = JSON.stringify(
          this.$store.state.app.pageOpenedList
        );
        let lastPageName = "";
        let length = this.$store.state.app.pageOpenedList.length;
        if (length > 1) {
          lastPageName = this.$store.state.app.pageOpenedList[length - 1].name;
        } else {
          lastPageName = this.$store.state.app.pageOpenedList[0].name;
        }
        this.$router.push({
          name: lastPageName
        });
      }
    },
    mounted() {}
  };
  </script>
  
  <style lang="less">
  .change-pass {
    &-btn-box {
      margin-bottom: 10px;
  
      button {
        padding-left: 0;
  
        span {
          color: #2D8CF0;
          transition: all .2s;
        }
  
        span:hover {
          color: #0C25F1;
          transition: all .2s;
        }
      }
    }
  }
  </style>
  