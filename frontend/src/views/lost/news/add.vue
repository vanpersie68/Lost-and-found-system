<template>
<div>
    <Card>
        <div slot="title">
            <div class="edit-head">
                <a @click="close" class="back-title">
                    <Icon type="ios-arrow-back" />Back
                </a>
                <div class="head-name">Add Announcement</div>
                <span></span>
                <a @click="close" class="window-close">
                    <Icon type="ios-close" size="31" class="ivu-icon-ios-close" />
                </a>
            </div>
        </div>
        <Form ref="form" :model="form" :label-width="100" :rules="formValidate" label-position="left">
            <FormItem label="Announcement Title" prop="title">
                <Input v-model="form.title" clearable show-word-limit maxlength="240" placeholder="Please enter announcement title..." style="width:570px" />
            </FormItem>
            <FormItem label="Announcement Content" prop="content">
                <Input v-model="form.content" clearable type="textarea" :rows="10" show-word-limit maxlength="240" placeholder="Please enter announcement content..." style="width:570px" />
            </FormItem>
            <FormItem label="Publish Status" prop="status">
                <Select v-model="form.status" clearable placeholder="Please select publish status..." style="width:570px">
                    <Option value="Normal">Normal</Option>
                    <Option value="Archived">Archived</Option>
                </Select>
            </FormItem>
            <FormItem label="Sort Order" prop="sortOrder">
                <InputNumber v-model="form.sortOrder" min="0" max="5000000" placeholder="Please enter sort order..." style="width:570px"></InputNumber>
            </FormItem>
            <Form-item class="br">
                <Button @click="handleSubmit" :loading="submitLoading" type="primary">Submit and Save</Button>
                <Button @click="handleReset">Reset</Button>
                <Button type="dashed" @click="close">Close</Button>
            </Form-item>
        </Form>
    </Card>
</div>
</template>

<script>
import {
    addNews
} from "./api.js";
export default {
    name: "add",
    components: {},
    data() {
        return {
            submitLoading: false, // 表单提交状态
            form: { // 添加或编辑表单对象初始化数据
                title: "",
                content: "",
                status: "Normal",
                sortOrder: 0,
            },
            // 表单验证规则
            formValidate: {}
        };
    },
    methods: {
        init() {},
        handleReset() {
            this.$refs.form.resetFields();
        },
        handleSubmit() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    addNews(this.form).then(res => {
                        this.submitLoading = false;
                        if (res.success) {
                            this.$Message.success("successful operation");
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
