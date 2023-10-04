<template>
    <div>
        <Card>
            <p slot="title">Edit</p>
            <Form ref="form" :model="form" :label-width="90" :rules="formValidate" style="position:relative">
                <FormItem label="Name" prop="name">
                    <Input v-model="form.name" style="width: 400px" />
                </FormItem>
                <Form-item class="br">
                    <Button @click="handleSubmit" :loading="submitLoading" type="primary">Submit and Save</Button>
                    <Button @click="handleReset">Reset</Button>
                </Form-item>
                <Spin size="large" fix v-if="loading"></Spin>
            </Form>
        </Card>
    </div>
    </template>
    
    <script>
    export default {
        name: "edit",
        data() {
            return {
                loading: true, // Form loading state
                submitLoading: false, // Form submission state
                form: {
                    id: "",
                    name: ""
                },
                // Form validation rules
                formValidate: {
                    name: [{
                        required: true,
                        message: "Cannot be empty",
                        trigger: "blur"
                    }]
                },
                backRoute: ""
            };
        },
        methods: {
            init() {
                this.handleReset();
                this.form.id = this.$route.query.id;
                this.backRoute = this.$route.query.backRoute;
                this.getData();
            },
            handleReset() {
                this.$refs.form.resetFields();
            },
            getData() {
                this.loading = true;
                // this.getRequest("Request URL, e.g., /getById/" + this.form.id).then(res => {
                //   this.loading = false;
                //   if (res.success) {
                //     // Convert null to ""
                //     let v = res.result
                //     for (let attr in v) {
                //       if (v[attr] == null) {
                //         v[attr] = "";
                //       }
                //     }
                //     let str = JSON.stringify(v);
                //     let data = JSON.parse(str);
                //     this.form = data;
                //   }
                // });
                // Simulate successful data retrieval
                this.loading = false;
                if (this.form.id == "1") {
                    this.form.name = "zwz";
                } else {
                    this.form.name = "zwz";
                }
            },
            handleSubmit() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        // Simulate success
                        this.submitLoading = false;
                        this.$Message.success("Edit successful");
                        this.closeCurrentPage();
                    }
                });
            },
            // Close the current page
            closeCurrentPage() {
                this.$store.commit("removeTag", "edit");
                localStorage.pageOpenedList = JSON.stringify(
                    this.$store.state.app.pageOpenedList
                );
                this.$router.push({
                    name: this.backRoute
                });
            }
        },
        watch: {
            // Listen for route changes and retrieve data based on id
            $route(to, from) {
                if (to.name == "edit") {
                    this.handleReset();
                    this.form.id = this.$route.query.id;
                    this.getData();
                }
            }
        },
        mounted() {
            this.init();
        }
    };
    </script>
    