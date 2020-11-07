<template>
    <div style="flex: 1">
        <web style="flex: 1" :src="src | getSrc"></web>
    </div>
</template>

<script>
  let path =require('weexplus-render')


    export default {
        props: {
            src:{


                default:''

            }
        },
        data() {
            return {}
        },
        filters:{
            getSrc(src){
              return path.default.path.getPath(src)
            }
        },
        methods: {
            onLoad(param) {

            },
            onShow() {

            },
            onHide() {

            },
            onUnload() {

            }
        },
        created() {

        }
    }
</script>

<style scoped>

</style>
