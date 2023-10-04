<style lang="less">
</style>

<template>
<div>
    <Card>
        <!-- Card Title -->
        <p slot="title">Add</p>
        <Form ref="form" :model="form" :label-width="90" :rules="formValidate">
            <!-- Form Item: Name -->
            <FormItem label="Name" prop="name">
                <Input v-model="form.name" style="width: 400px" />
            </FormItem>
            <!-- Form Buttons -->
            <Form-item class="br">
                <!-- Submit Button -->
                <Button @click="handleSubmit" :loading="submitLoading" type="primary">Submit and Save</Button>
                <!-- Reset Button -->
                <Button @click="handleReset">Reset</Button>
            </Form-item>
        </Form>
    </Card>
</div>
</template>

<script>
export default {
    name: "add",
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
                    message: "Cannot be empty", // Error message if empty
                    trigger: "blur" // Trigger validation on blur
                }]
            },
            backRoute: "" // Route to navigate back to
        };
    },
    methods: {
        init() {
            this.backRoute = this.$route.query.backRoute; // Initialize the route to navigate back to
        },
        handleReset() {
            this.$refs.form.resetFields(); // Reset the form fields
        },
        handleSubmit() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    // Simulate success (you can replace this with actual form submission)
                    this.submitLoading = false;
                    this.$Message.success("Added successfully");
                    this.closeCurrentPage(); // Close the current page after successful submission
                }
            });
        },
        // Close the current page
        closeCurrentPage() {
            this.$store.commit("removeTag", "add"); // Remove the 'add' tag from the store (if used for managing open pages)
            localStorage.pageOpenedList = JSON.stringify(
                this.$store.state.app.pageOpenedList
            ); // Save the updated page list to localStorage (if used for managing open pages)
            this.$router.push({
                name: this.backRoute // Navigate back to the specified route
            });
        }
    },
    watch: {
        $route(to, from) {
            if (to.name == "add") {
                this.handleReset(); // Reset the form when the route is 'add'
            }
        }
    },
    mounted() {
        this.init(); // Initialize the component
    }
};
</script>
