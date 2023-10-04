<template>
    <div class="login">
      <Row justify="center" align="middle" @keydown.enter.native="submitRegist" style="height:100%">
        <div class="loginUp">
          <div class="loginLeft">
            <img src="../assets/login/logo.png" alt="" srcset="" />
            <span class="line"></span>
            <span class="title">Campus Lost and Found Management System</span>
          </div>
        </div>
        <div class="loginMiddle">
          <div class="login-background">
            <div class="loginBg"></div>
            <div class="loginRight">
              <Row v-if="!socialLogining" class="loginRow">
                <Form ref="usernameLoginForm" :model="form" :rules="rules" class="form" style="width:100%">
                  <FormItem prop="username" class="loginInput">
                    <Row>
                      <Input v-model="form.username" size="large" clearable placeholder="Your mobile number" autocomplete="off">
                        <Icon class="iconfont icon-user" slot="prefix" style="line-height:50px" />
                      </Input>
                    </Row>
                  </FormItem>
                  <FormItem prop="nickname" class="loginInput">
                    <Row>
                      <Input v-model="form.nickname" size="large" clearable placeholder="Your name" autocomplete="off">
                        <Icon class="iconfont icon-user" slot="prefix" style="line-height:50px" />
                      </Input>
                    </Row>
                  </FormItem>
                  <FormItem prop="password">
                    <Input style="height:50px;line-height:50px" type="password" v-model="form.password" size="large" placeholder="Your password" password autocomplete="off">
                      <Icon class="iconfont icon-lock" slot="prefix" style="line-height:50px" />
                    </Input>
                  </FormItem>
                  <FormItem prop="code">
                    <Row type="flex" justify="space-between" style="align-items: center;overflow: hidden;">
                      <Col span="16">
                        <Input v-model="form.code" size="large" clearable placeholder="Enter the image code" style="width:90%" :maxlength="10" class="input-verify" />
                      </Col>
                      <Col span="8">
                        <div class="code-image" style="position:relative;font-size:12px;width:100%">
                          <Spin v-if="loadingCaptcha" fix></Spin>
                          <img :src="captchaImg" @click="getCaptchaImg" alt="Failed to load the code" style="width:110px;cursor:pointer;display:block" />
                        </div>
                      </Col>
                    </Row>
                  </FormItem>
                </Form>
                <Row>
                  <Button class="login-btn" type="primary" size="large" :loading="loading" @click="submitRegist" long>
                    <span v-if="!loading" style="letter-spacing:20px; font-weight:bold">Register</span>
                    <span v-else>Registering</span>
                  </Button>
                </Row>
                <Row>
                  <router-link to="/login">
                    <Button class="login-btn" type="primary" size="large" long>Back to Login</Button>
                  </router-link>
                </Row>
              </Row>
              <p class="loginBottom">
                ART YOUR DREAM
              </p>
              <div v-if="socialLogining">
                <RectLoading />
              </div>
            </div>
          </div>
        </div>
        <div class="loginDown">
          <p style="margin-top:10px">Help | Privacy | Terms</p>
          <p>
            <span>Copyright © 2020 - Present XXX All rights reserved</span>
            <span style="display:inline-block; width:4px;height:6px"></span>
            <a target="_blank" href="https://beian.miit.gov.cn" style="color:#848585">ICP Record Zhejiang ICP XXXXXXXXXXX号</a>
            <a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=XXXXXXXXXXXXXX号">
              <img src="../assets/login/gonganlogo.png" style="margin-left:6px" />
              <p style="display:inline-block;color:#848585">Zhejiang Public Network Security XXXXXXXXXXXXXX号</p>
            </a>
          </p>
        </div>
      </Row>
    </div>
  </template>
  
  <script>
  import {
    regist,
    drawCodeImage,
    initCaptcha
  } from "@/api/index";
  import {
    validateMobile,
    validatePassword
  } from "@/libs/validate";
  export default {
    components: {},
    data() {
      return {
        captchaId: "",
        captchaImg: "",
        error: false,
        loading: false,
        errorCode: "",
        form: {
          username: "",
          password: "",
          mobile: "",
          code: "",
          captchaId: ""
        },
        rules: {
          username: [{
              required: true,
              message: "Please enter your mobile number",
              trigger: "blur"
            },
            {
              validator: validateMobile,
              trigger: "blur"
            }
          ],
          nickname: [{
            required: true,
            message: "Please enter your name",
            trigger: "blur"
          }],
          password: [{
              required: true,
              message: "Please enter your login password",
              trigger: "blur"
            },
            {
              validator: validatePassword,
              trigger: "blur"
            }
          ]
        }
      };
    },
    methods: {
      getCaptchaImg() {
        this.loadingCaptcha = true;
        initCaptcha().then(res => {
          this.loadingCaptcha = false;
          if (res.success) {
            this.captchaId = res.result;
            this.captchaImg = drawCodeImage + this.captchaId;
          }
        });
      },
      submitRegist() {
        this.form.captchaId = this.captchaId;
        this.form.mobile = this.form.username;
        this.$refs.usernameLoginForm.validate(valid => {
          if (valid) {
            if (!this.form.code) {
              this.errorCode = "Verification code cannot be empty";
              return;
            } else {
              this.errorCode = "";
            }
            this.loading = true;
            regist(this.form).then(res => {
              this.loading = false;
              if (res.success) {
                this.$router.push({
                  name: "login"
                });
              } 
            });
          }
        });
      }
    },
    mounted() {
      this.getCaptchaImg();
    }
  };
  </script>
  
  <style lang="less">
  html,body{
    background: #ffffff !important;
    font-family: Microsoft YaHei;
    font-weight: 400;;
  }
  a{   
    font-family: Microsoft YaHei;
    color: #77C8C6;
  }
  input::-webkit-input-placeholder {
    font-size: 14px;
  }
  .ivu-checkbox-wrapper.ivu-checkbox-large{
    font-size: 14px;
  }
  a:hover{
    font-family: Microsoft YaHei;
    color: #77C8C6;
  }
  .login {
    height: 100%;
    background-color: #ffffff;
  
    .ivu-tabs-nav-container {
      line-height: 2;
      font-size: 17px;
      box-sizing: border-box;
      white-space: nowrap;
      overflow: hidden;
      position: relative;
      zoom: 1;
    }
  
    // loginUp login logo
    .loginUp{
      width: 1200px;
      min-height: 80px;
      background-color:#ffffff;
      margin: 0 auto;
      overflow: hidden;
    }
    .loginLeft{
      margin-top: 20px;
      height: 50px;
      display: flex;
    }
    .line{
      display: inline-block;
      width: 2px;
      height: 25px;
      background: url(../assets/login/line.png);
      margin: 0px 10px;
      margin-top: 15px;
    }
    .title{
      line-height: 58px; 
      font-size: 18px;
      font-family: Microsoft YaHei;
      font-weight: 500;
      color: #999999;
    }
    .loginMiddle{
      width: 100%;
      height: 780px;
      margin: 0 auto; 
      background-color: #6a6ad2;
      overflow: hidden;
      // background: linear-gradient(45deg, rgba(2, 173, 168, 0.17), rgba(0, 221, 215, 0.17)); 
    }     
    .login-background{
      width: 1200px;
      height: 780px;
      margin: 0 auto;   
      display: flex;
      justify-content: space-between;
    }
    .loginBg{
      width: 560px;
      height: 684px;
      margin-top: -20px;
      background-image: url(../assets/login/star.png);
      background-repeat: no-repeat;
      background-position: left bottom;
    }
    .loginRight{
      width: 450px;
      height: 550px;
      background-color: #ffffff;
      border: 1px solid #E6E6E6;
      box-shadow: 0px 2px 15px 1px rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      margin-top: 115px;
      position: relative;
    }
    .loginRow{
      padding: 0px 30px;
    }
    .loginDown{
      width: 1200px;
      height: auto;
      margin: 0 auto;
  
    }
    .loginTab{
      margin-top: 20px;
    }
    .ivu-tabs-tab{
      color: #333333;     
      font-size: 18px;
      font-family: Microsoft YaHei;
      font-weight: bold;
    }
    .ivu-tabs-nav .ivu-tabs-tab{
      padding: 8px 42px;
      margin-right: 0px;
    }
    .ivu-tabs-ink-bar{
      height: 4px;
      width: 86px !important;
      border-radius: 2px;
      margin: 0px 42px;
      background-color: #6a6ad2;
    }
    .ivu-tabs-nav .ivu-tabs-tab-active,.ivu-tabs-nav .ivu-tabs-tab:hover{
      color: #333333;   
    }
    .loginInput{
      font-size: 18px;    
      font-family: Microsoft YaHei;
      font-weight: bold;
      color: #333333;
    }
  
    .ivu-tabs-bar{
      border-bottom: 0px;
    }
    .login-btn{
      width: 390px;
      height: 50px;
      background: radial-gradient(circle, #77C8C6, #50C7C4);
      border: 2px solid #61C8C5;
      box-shadow: 0px 2px 6px 0px rgba(0, 0, 0, 0.21);
      border-radius: 4px;
    }
    .login .login-btn, .login .other-login{
      margin-top: 40px;
    }
    .loginBottom{
      width: 448px;
      height: 60px;      
      background: #f9f9f9;
      border-radius: 0px 0px 5px 5px;
      padding: 0px;
      position: absolute;
      bottom: 0px;  
      font-size: 16px;
      font-weight: bold;
      color: #6a6ad2;
      text-align: center;
      line-height: 60px;
    }
    .loginDown p{
      text-align: center;       
      font-size: 12px;
      font-family: Microsoft YaHei;
      color: #777777;
      line-height: 22px;
    }
    .ivu-checkbox-checked .ivu-checkbox-inner{
      background-color: #6a6ad2;
      border-color: #6a6ad2;
    }
    .ivu-form-item{
      margin-bottom: 24px;
    }
    .ivu-input-wrapper-large .ivu-input-icon{
      line-height: 50px;
    }
    .loginInput input:nth-of-type(1){
      height: 50px;  
      font-size: 18px;
      font-weight: bold;
      font-family: Microsoft YaHei;
      color: #333333;
      line-height: 50px;
    }
    .ivu-input-large{
      height: 50px;  
      color:#CFCFCF;
      line-height: 50px;
    }
    .ivu-input-large{
      font-size: 14px;
    }
    .ivu-btn-large{
      height: 50px;
    }
    .form {
      padding-top: 2vh;
      .input-verify {
        width: 67%;
      }
    }
    .code-image {
      .ivu-spin-fix .ivu-spin-main {
        height: 20px;
      }
    }
    .forget-pass,
    .other-way {
      font-size: 14px;
    }
    .login-btn,
    .other-login {
      margin-top: 40px;
    }
    .icons {
      display: flex;
      align-items: center;
    }
    .other-icon {
      cursor: pointer;
      margin-left: 10px;
      display: flex;
      align-items: center;
      color: rgba(0, 0, 0, .2);
  
      :hover {
        color: #2d8cf0;
      }
    }
    .layout {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      width: 368px;
      height: 100%;
    }
  }
  </style>
  