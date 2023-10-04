<template>
<div class="own-space">
    <Card>
        <Divider dashed>
            Personal portal
            <Button type="success" v-show="!editFlag" @click="editFlag = true">Start editing</Button>
            <Button type="warning" v-show="editFlag" @click="saveEdit">Save submission</Button>
        </Divider>
        <Form ref="userForm" :model="userForm" :label-width="90" label-position="left">
            <Row :gutter="16">
                <Col :span="12">
                <FormItem label="Login account" prop="username">
                    <Input v-model="userForm.username" readonly style="width: 100%" />
                </FormItem>
                </Col>
                <Col :span="12">
                <FormItem label="Name" prop="nickname">
                    <Input v-model="userForm.nickname" readonly style="width: 100%" />
                </FormItem>
                </Col>
            </Row>
            <Row :gutter="16">
                <Col :span="12">
                <FormItem label="Account type" prop="typeTxt">
                    <Input v-model="userForm.typeTxt" readonly style="width: 100%" />
                </FormItem>
                </Col>
                <Col :span="12">
                <FormItem label="Department" prop="departmentTitle">
                    <Input v-model="userForm.departmentTitle" readonly style="width: 100%" />
                </FormItem>
                </Col>
            </Row>
            <Row :gutter="16">
                <Col :span="12">
                <FormItem label="Gender" prop="username">
                    <dict dict="sex" v-model="userForm.sex" transfer :readonly="!editFlag" placeholder="Please select gender" style="width: 100%" />
                </FormItem>
                </Col>
                <Col :span="12">
                <FormItem label="Avatar" prop="avatar">
                    <upload-pic-thumb v-model="userForm.avatar" :multiple="false" style="width: 100%"></upload-pic-thumb>
                </FormItem>
                </Col>
            </Row>
            <Row :gutter="16">
                <Col :span="12">
                <FormItem label="Family address" prop="nickname">
                    <Input v-model="userForm.street" :readonly="!editFlag" style="width: 100%" />
                </FormItem>
                </Col>
                <Col :span="12">
                <FormItem label="Birthday" prop="username">
                    <DatePicker v-model="userForm.birth" @on-change="changeBirth" :readonly="!editFlag" style="width: 100%" type="date"></DatePicker>
                </FormItem>
                </Col>
            </Row>
            <Row :gutter="16">
                <Col :span="24">
                <FormItem label="Signature" prop="nickname">
                    <Input v-model="userForm.description" type="textarea" :readonly="!editFlag" style="width: 100%" :autosize="{minRows: 3,maxRows: 5}" placeholder="Signature"></Input>
                </FormItem>
                </Col>
            </Row>
        </Form>
    </Card>
    <Divider>Personal portal</Divider>
    <Card>
        <Row :gutter="16">
            <Col span="12">
            <Divider>Currently commonly used modules</Divider>
            <List header="---" footer="---" border>
                <ListItem v-for="(item, index) in addMenuTempList" :key="index">
                    {{ item.title }}
                    <Button @click="deleteOneMenu(item)" type="error" shape="circle" icon="md-trash" size="small" style="marginLeft:20px;"></Button>
                </ListItem>
            </List>
            </Col>
            <Col span="12">
            <Divider>Personal portal settings
                <Button @click="addMenuFx" type="primary">Save changes</Button></Divider>
            <Row :gutter="16">
                <div class="desc">Complete the selection menu below to add</div>
                <search theme="light" type="input" @changeOk="addMenuTempFx" />
            </Row>
            </Col>
        </Row>
    </Card>
</div>
</template>

<script>
import {
    getMyDoorList,
    setMyDoorList,
    userInfoEdit
} from "./api.js";
import uploadPicThumb from "@/views/template/upload-pic-thumb";
import Cookies from "js-cookie";
import dict from "@/views/template/dict";
import search from "./search.vue";
export default {
    components: {
        uploadPicThumb,
        dict,
        search
    },
    name: "my_home_index",
    data() {
        return {
            editFlag: false,
            userForm: {
                id: "",
                avatar: "",
                username: "",
                sex: "",
                status: "",
                type: "",
                typeTxt: "",
                address: "",
                addressArray: []
            },
            saveLoading: false,
            addMenuTempList: []
        };
    },
    methods: {
        init() {
            let v = JSON.parse(Cookies.get("userInfo"));
            for (let attr in v) {
                if (v[attr] == null) {
                    v[attr] = "";
                }
            }
            let str = JSON.stringify(v);
            let userInfo = JSON.parse(str);
            userInfo.addressArray = [];
            this.userForm = userInfo;
            if (userInfo.address) {
                this.userForm.address = userInfo.address;
                this.userForm.addressArray = JSON.parse(userInfo.address);
            }
            if (this.userForm.type == 0) {
                this.userForm.typeTxt = "General user";
            } else if (this.userForm.type == 1) {
                this.userForm.typeTxt = "Administrator";
            }
        },
        addMenuTempFx(e) {
            if (this.addMenuTempList.length > 5) {
                this.$Message.warning("Add up to 6 commonly used modules");
            } else {
                this.addMenuTempList.push(e);
            }
        },

        addMenuFx() {
            var str = "";
            var size = this.addMenuTempList.length;
            if (size > 0) {
                str += this.addMenuTempList[0].name;
                for (var i = 1; i < size; i++) {
                    str += "ZWZ666" + this.addMenuTempList[i].name;
                }
            }
            setMyDoorList({
                str: str
            }).then((res) => {
                if (res.success) {
                    this.$Message.warning("Successful operation");
                }
            });
        },
        saveEdit() {
            this.saveLoading = true;
            this.editFlag = false;
            if (typeof this.userForm.birth == "object") {
                this.userForm.birth = this.format(this.userForm.birth, "yyyy-MM-dd");
            }
            delete this.userForm.roles;
            userInfoEdit(this.userForm).then(res => {
                this.saveLoading = false;
                if (res.success) {
                    this.$Message.success("Saved successfully");
                    // 更新用户信息
                    Cookies.set("userInfo", JSON.stringify(this.userForm));
                    // 更新头像
                    this.$store.commit("setAvatarPath", this.userForm.avatar);
                }
            });
        },

        getMyDoorListFx() {
            var that = this;
            getMyDoorList().then((res) => {
                that.addMenuTempList = res.result;
            });
        },
        deleteOneMenu(e) {
            var array = [];
            for (var i = 0; i < this.addMenuTempList.length; i++) {
                if (this.addMenuTempList[i].name != e.name) {
                    array.push(this.addMenuTempList[i]);
                }
            }
            this.addMenuTempList = array;
        },
        changeAddress() {
            this.userForm.address = JSON.stringify(this.userForm.addressArray);
        },
        changeBirth(v) {
            this.userForm.birth = v;
        }
    },
    mounted() {
        this.init();
    }
};
</script>

<style lang="less" scoped>

</style>
