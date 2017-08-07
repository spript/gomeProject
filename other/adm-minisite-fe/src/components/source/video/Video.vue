<template>
</template>
<script>
    
</script>
<style lang="less">
    @import '../../../assets/css/base.less';
    .videoDetail{
        width:100%;
    }
    .video-box {
        position: relative;
        background: #fff;
    }

    .video-box img {
        width: 100%;
    }
    .video_header{
        background: #fff;
        h2{
            font-size: .42rem;
            color: #333;
            margin-top: .3rem;
            margin-bottom: .2rem;
        }
        border-bottom: 1px solid #ddd;
    }
    .share_tips{
        overflow: hidden;
        li{
            float:left;
            font-size: .18rem;
            margin-right: .18rem;
            &:first-child{
                color: #ff3a64;
                
            }
            &:last-child{
                color: #a07aff;
            }
            color: #ffa229;
        }
    }
    .video_content{
        padding: 0 .22rem;
        padding-bottom: .4rem;
        p{
            font-size: .26rem;
            color: #666;
            margin-top: .24rem;
            line-height: 1.67;
        }
    }

    .video-box .video-play {
        position: absolute;
        top: 50%;
        left: 50%;
        -webkit-transform: translate3d(-50%,-50%,0);
        transform: translate3d(-50%,-50%,0);
        width: 1.4rem;
        height: 1.4rem;
        background-color: rgba(0,0,0,.7);
        border-radius: .08rem;
        color: #fff;
        text-align: center
    }

    .video-box .video-play .iconmoon {
        display: block;
        font-size: .4rem;
        margin: .26rem auto .2rem
    }

    .video-box .video-play strong {
        font-weight: 400;
        font-size: .24rem
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
            font-size: .32rem;
            -ms-flex: 1;
            flex: 1;
            line-height: 1rem;
            text-decoration: none;
            &:last-child{
                background: #ec6c6e;
                color: #fff;
            }
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
    .video_list{
        margin: .18rem 0;
        margin-bottom: 0;
        border-top: 1px solid #ddd;
        border-bottom: 1px solid #ddd;
        background: #fff;
        padding: 0 .22rem;
        
        ul{
            li{
                margin-top: .5rem;
                padding-bottom: .5rem;
                border-bottom: 1px solid #ddd;
                overflow:hidden;
                &:last-child{
                    border-bottom: 0 none;
                }
                .shop_item_right{
                    float: right;
                    width: 4.25rem;
                    h2{
                        font-size: .42rem;
                        color: #333;
                        float:left;
                    }
                    p{
                        font-size: .26rem;
                        color: #999;
                        margin-top: .43rem;
                        margin-bottom: .2rem;
                        float:left;
                    }
                    span{
                        font-size: .4rem;
                        color: #ea5c5e;
                        float:left;
                    }
                }
                img{
                    width: 2.4rem;
                    float: left;
                    margin-right: .4rem;
                }
            }
        }
    }
    .video_templet_shop{
        padding-top: .5rem;
        padding-bottom: .6rem;
        background: #fff;
        margin: .18rem 0;
        border-top: 1px solid #ddd;
        border-bottom: 1px solid #ddd;
        .video_inner{
            width: 5.2rem;
            margin: 0 auto;
            background: #e8fbe8;
            .goShop{
                font-size: .28rem;
                color: #ea5c5e;
                border: 1px solid #ea5c5e;
                padding: .16rem .34rem;
                border-radius: .6rem;
                display: block;
                margin: 0 auto;
                width: 1.8rem;
                text-align: center;
                margin-top: .4rem;
            }
            h2{
                font-size: .36rem;
                color: #333;
                text-align: center;
                margin-top: .3rem;
                margin-bottom: .26rem;
            } 
            p{
                font-size: .26rem;
                color: #333;
                line-height: 1.67;
            }
            img{
                width: 1.3rem;
                height: 1.3rem;
                margin: 0 auto;
                display: block;
            }
        }
    }
</style>