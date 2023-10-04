<template>
<div>
    <Card>
        <div slot="title">
            <div class="edit-head">
                <a @click="close" class="back-title">
                    <Icon type="ios-arrow-back" />Return
                </a>
                <div class="head-name">Add found items</div>
                <span></span>
                <a @click="close" class="window-close">
                    <Icon type="ios-close" size="31" class="ivu-icon-ios-close" />
                </a>s
            </div>
        </div>
        <Form ref="form" :model="form" :label-width="100" :rules="formValidate" label-position="left">
            <FormItem label="Item Name" prop="title">
                <Input v-model="form.title" clearable show-word-limit maxlength="240" placeholder="Please enter the item name..." style="width:570px" />
            </FormItem>
            <FormItem label="Pick up time" prop="time">
                <DatePicker type="date" format="yyyy-MM-dd" @on-change="changeDate" clearable placeholder="Please select the pickup time..." style="width:570px"></DatePicker>
            </FormItem>
            <FormItem label="Pick up location" prop="address">
                <Input v-model="form.address" clearable show-word-limit maxlength="240" placeholder="Please enter the location where the item was picked up..." style="width:570px" />
            </FormItem>
            <FormItem label="Type" prop="type">
                <Select v-model="form.type" clearable style="width:570px">
                    <Option value="Item type 1">Item type 1</Option>
                    <Option value="Item type 2">Item type 2</Option>
                    <Option value="Item type 3">Item type 3</Option>
                </Select>
            </FormItem>
            <FormItem label="Related description" prop="content">
                <Input v-model="form.content" type="textarea" :rows="4" show-word-limit maxlength="240" placeholder="Please enter a description of the item..." clearable style="width:570px" />
            </FormItem>
            <FormItem label="Temporary location" prop="pushAddress">
                <Input v-model="form.pushAddress" type="textarea" :rows="4" show-word-limit maxlength="240" placeholder="Please enter the temporary storage location of the items..." clearable style="width:570px" />
            </FormItem>
            <FormItem label="Item pictures" prop="image">
                <upload-pic-input v-model="form.image" placeholder="Please upload a picture of the item..." style="width:570px"></upload-pic-input>
            </FormItem>
            <Form-item class="br">
                <Button @click="handleSubmit" :loading="submitLoading" type="primary">Submit and save</Button>
                <Button @click="handleReset">Reset</Button>
                <Button type="dashed" @click="close">Close</Button>
            </Form-item>
        </Form>
    </Card>
</div>
</template>

<script>
import {
    addDraw
} from "./api.js";
import uploadPicInput from "@/views/template/upload-pic-input";
export default {
    name: "add",
    components: {
        uploadPicInput,
    },
    data() {
        return {
            submitLoading: false, // 表单提交状态
            form: { // 添加或编辑表单对象初始化数据
                title: "",
                time: "",
                address: "",
                type: "Item type 1",
                content: "",
                pushAddress: "",
                image: "",
            },
            // 表单验证规则
            formValidate: {}
        };
    },
    methods: {
        init() {},
        changeDate(e) {
            this.form.time = e;
        },
        handleReset() {
            this.$refs.form.resetFields();
        },
        handleSubmit() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    addDraw(this.form).then(res => {
                        this.submitLoading = false;
                        if (res.success) {
                            this.$Message.success("Operation success");
                            this.submited();
                        }
                    });
                }
            });
        },
        close() {
            this.$emit("close", true);
        },
        submited() {
            this.$emit("submited", true);
        }
    },
    mounted() {
        this.init();
    }
};
</script>

<style lang="less">
// 建议引入通用样式 具体路径自行修改 可删除下面样式代码
// @import "../../../styles/single-common.less";
.edit-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    position: relative;

    .back-title {
        color: #515a6e;
        display: flex;
        align-items: center;
    }

    .head-name {
        display: inline-block;
        height: 20px;
        line-height: 20px;
        font-size: 16px;
        color: #17233d;
        font-weight: 500;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .window-close {
        z-index: 1;
        font-size: 12px;
        position: absolute;
        right: 0px;
        top: -5px;
        overflow: hidden;
        cursor: pointer;

        .ivu-icon-ios-close {
            color: #999;
            transition: color .2s ease;
        }
    }

    .window-close .ivu-icon-ios-close:hover {
        color: #444;
    }
}
</style>
