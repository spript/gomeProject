<template>
</template>
<script>
    
</script>
<style lang="less">
    @import '../../../assets/css/base.less';
    
    .shopDetail{
        width: 100%;
        .shop_info{
            padding: 0 1.1rem;
            font-size: .28rem;
            color: #666;
            line-height: .42rem;
            padding-bottom: .5rem;
            background: #fff;
            p{
                padding: 0 !important;
            }

        }
        .shop_content{
            background: #fff;
            padding: .4rem 0;
            img{
                width: 100%;
                display: block;
            }
            h2{
                font-size: .42rem;
                color: #333;
                margin-top: .4rem;
                margin-bottom: .26rem;
                text-align: center;
            }
            a.info_btn{ 
                display: block;
                width: 3rem;
                height: .6rem;
                font-size: .28rem;
                color: #ea5c5e;
                border: 1px solid #ea5c5e;
                border-radius: .3rem;
                text-align: center;
                line-height: .6rem;
                margin: 0 auto;
                margin-bottom: .26rem;
            }
            p{
                font-size: .28rem;
                color: #666;
                line-height: .42rem;
                padding: 0 .22rem;
                margin-bottom: .5rem;
            }
        }
        
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
        .shop_header{
            width: 100%;
            height: 4.54rem;
            background: #fff;
            text-align: center;
            
            .shop_inner{
                
                padding-top: .2rem;
                margin: 0 auto;
                height: 4.54rem;
                .share_tips{
                    overflow: hidden;
                    display: -webkit-box;
                    display: -ms-flexbox;
                    display: flex;
                    justify-content: center;
                    div{
                        float: left;
                        font-size: .18rem;
                        &:first-child{
                            color: #ff3a64;
                            margin-right: .18rem;
                        }
                        color: #ffa229;
                    }
                }
                .shop_icon{
                    width: 1.3rem;
                    height: 1.3rem;
                    display: block;
                    margin: 0 auto;
                    margin-bottom: .46rem;
                }
                h2{
                    font-size: .36rem;
                    color: #333;
                    margin-bottom: .18rem;
                }
                p{
                    font-size: .24rem;
                    color: #666;
                    margin-top: .18rem;
                }
                a {
                    width: 2rem;
                    display: block;
                    height: .6rem;
                    font-size: .28rem;
                    color: #fff;
                    background: #ea5c5e;
                    text-align: center;
                    line-height: .6rem;
                    border-radius: .3rem;
                    margin: 0 auto;
                    margin-top: .3rem;
                }
            }
        }
        .bottom_tools{
            background-color: #fff;
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            text-align: center;
            height: 1rem;
            border-top: 1px solid #e2e2e2;
            overflow: hidden;
            .toolsItem{
            display: block;
            -webkit-box-flex: 1;
            -ms-flex: 1;
            font-size: .32rem;
            flex: 1;
            line-height: 1rem;
            text-decoration: none;
            &:last-child{
                background: #ec6c6e;
                color: #fff;
            }
            }
        }
    }
</style>