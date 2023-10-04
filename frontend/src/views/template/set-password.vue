<template>
    <div class="set-password">
        <Poptip transfer trigger="focus" placement="right" width="250">
            <Input type="password" password :maxlength="maxlength" v-model="currentValue" @on-change="handleChange" :size="size" :placeholder="placeholder" :disabled="disabled" :readonly="readonly" />
            <div :class="tipStyle" slot="content">
                <div class="words">Strength: {{strength}}</div>
                <Progress :percent="strengthValue" :status="progressStatus" hide-info style="margin: 13px 0;" />
            </div>
        </Poptip>
    </div>
    </template>
    
    <script>
    export default {
        name: "setPassword",
        props: {
            value: String,
            size: String,
            placeholder: {
                type: String,
                default: "Enter password, length is 6-20 characters"
            },
            disabled: {
                type: Boolean,
                default: false
            },
            readonly: {
                type: Boolean,
                default: false
            },
            maxlength: {
                type: Number,
                default: 20
            }
        },
        data() {
            return {
                currentValue: this.value,
                tipStyle: "password-tip-none",
                strengthValue: 0,
                progressStatus: "normal",
                strength: "None",
                grade: 0
            };
        },
        methods: {
            checkStrengthValue(v) {
                // Password strength rating, maximum 5
                let grade = 0;
                if (/\d/.test(v)) {
                    grade++; // Digit
                }
                if (/[a-z]/.test(v)) {
                    grade++; // Lowercase letter
                }
                if (/[A-Z]/.test(v)) {
                    grade++; // Uppercase letter
                }
                if (/\W/.test(v)) {
                    grade++; // Special character
                }
                if (v.length >= 10) {
                    grade++;
                }
                this.grade = grade;
                return grade;
            },
            strengthChange() {
                if (!this.currentValue) {
                    this.tipStyle = "password-tip-none";
                    this.strength = "None";
                    this.strengthValue = 0;
                    return;
                }
                let grade = this.checkStrengthValue(this.currentValue);
                if (grade <= 1) {
                    this.progressStatus = "wrong";
                    this.tipStyle = "password-tip-weak";
                    this.strength = "Weak";
                    this.strengthValue = 33;
                } else if (grade >= 2 && grade <= 4) {
                    this.progressStatus = "normal";
                    this.tipStyle = "password-tip-middle";
                    this.strength = "Medium";
                    this.strengthValue = 66;
                } else {
                    this.progressStatus = "success";
                    this.tipStyle = "password-tip-strong";
                    this.strength = "Strong";
                    this.strengthValue = 100;
                }
            },
            handleChange(v) {
                this.strengthChange();
                this.$emit("input", this.currentValue);
                this.$emit("on-change", this.currentValue, this.grade, this.strength);
            },
            setCurrentValue(value) {
                if (value === this.currentValue) {
                    return;
                }
                this.currentValue = value;
                this.strengthChange();
                this.$emit("on-change", this.currentValue, this.grade, this.strength);
            }
        },
        watch: {
            value(val) {
                this.setCurrentValue(val);
            }
        }
    };
    </script>
    
    <style lang="less">
    .set-password .ivu-poptip,
    .set-password .ivu-poptip-rel {
        display: block;
    }
    
    .password-tip-none {
        padding: 1vh 0;
    }
    
    .password-tip-weak {
        padding: 1vh 0;
    
        .words {
            color: #ed3f14;
        }
    }
    
    .password-tip-middle {
        padding: 1vh 0;
    
        .words {
            color: #2d8cf0;
        }
    }
    
    .password-tip-strong {
        padding: 1vh 0;
    
        .words {
            color: #52c41a;
        }
    }
    </style>
    