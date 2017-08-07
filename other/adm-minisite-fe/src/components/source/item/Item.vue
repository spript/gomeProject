<template>
    <p>哈哈</p>
</template>
<script>
    
</script>
<style lang="less">
    @import '../../../assets/css/base.less';

    .itemDetail{
        .detail_footer{
            width: 100%;
            p{
                font-size: .26rem;
                color: #999; 
                height: 1rem;
                line-height: 1rem;
                text-align: center;
            }
        }
        width: 100%;
        .header_img{
            width: 100%;
            height: auto;
            background: #fff;
        }
        .header_box{
            background: #fff;
        }
        .detail_content{
            width: 100%;
            overflow: hidden;
            border-bottom: 1px solid #ddd;
            .content_header{
                padding: .3rem .22rem;
                padding-bottom: .4rem;
                background: #fff;
                border-bottom:1px solid #ddd;
                .p_box{
                    font-size: .26rem;
                    color: #333;
                    line-height: .4rem;
                }
            }
            .content_main{
                margin-top:.18rem;
                background: #fff;
                padding:0 .22rem;
                overflow: hidden;
                border-top: 1px solid #ddd;
                ul{
                    li{
                        width: 50%;
                        /*height: 3.36rem;*/
                        float:left;
                        padding-bottom: .56rem;
                        border-top: 1px solid #f0f0f0;
                        overflow:hidden;
                        -webkit-box-sizing: border-box;
	                    box-sizing: border-box;
                        border-right: 1px solid #f0f0f0;
                        &:nth-child(1){
                            border-top:0 none;
                        }
                        &:nth-child(2){
                            border-top:0 none;
                        }
                        img{
                            width: 3.36rem;
                            height: 3.36rem;
                        }
                        h2{
                            font-size: .26rem;
                            color:#666;
                            margin-bottom: .18rem;
                        }
                        p{
                            font-size: .26rem;
                            color: #ea5c5e;
                            font-weight: bold;
                        }
                        &:nth-child(even){
                            float:right;
                            text-align: right;
                            border-right: 0 none;
                            h2{
                               text-align: left; 
                               padding-left: .15rem;
                            }
                            p{
                                text-align: left;
                                padding-left: .15rem;
                            }
                        }
                    }
                }
                
            }
            .share_tips{
                overflow: hidden;
                margin-top: .16rem;
                margin-bottom: .2rem;
                li{
                    float:left;
                    font-size: .18rem;
                    &:first-child{
                        color: #ff3a64;
                        margin-right: .18rem;
                    }
                    color: #ffa229;
                }
            }
            /*padding: .3rem .22rem;*/
        }
        h2{
            font-size: .42rem;
            color: #333;
        }
    }
</style>