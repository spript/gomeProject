<template>
	<div class="amp-form amp-form03">
		<div class="form-column">
			<el-form  label-width="124px" :model="editData" ref="editData" :rules="rules">
				<!--模板1 默认-->
				<template v-if="editData.pageTemplateId == 1 || editData.pageTemplateId == 4">
					<el-form-item label="名称：" >
						<el-input  placeholder="请输入名称" style="width:400px;" v-model="editData.title"></el-input>
				    </el-form-item>
				    <el-form-item label="顶部大图：">
						<div class="detail-row" style="width: 146px;margin: 0 20px 0 0;float: left;">
							<div class="row-photo-s" @click="changeTopFile">
								<input id="topPicture" type="file"
									   style="width: 146px;height: 100px;display:block;position:absolute;visibility: hidden;"
									   @change="topPictureChange" accept="image/jpg,image/jpeg,image/png">
								<div class="photo-add photo-add-b"><img :src="imgUrl"></div>
								<div class="el-upload__tip" style="color:red;text-align:center;width:150px;">
									请上传宽高比例为2:1的图片
								</div>
							</div>
						</div>
					</el-form-item>
					<el-form-item label="文案区：" prop="description">
						<el-input type="textarea" v-model="editData.description" :autosize="{ minRows: 2, maxRows: 4}"
								  placeholder="" style="width:400px;">
						</el-input>
					</el-form-item>
				    <div v-for="index in 5">
						<el-form-item label="SKU ID：">
							<el-col :span="10" style="margin-right:30px;">
								<el-input v-model="editData.itemList[(index * 2) - 2].skuId" placeholder="请输入SKU ID"
										  @blur="queryItemId((index * 2) - 2)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[(index * 2) - 2].isError">
									{{videoShopSKUList[(index * 2) - 2].errorMsg }}
								</div>
							</el-col>
							<el-col :span="10">
								<el-input v-model="editData.itemList[(index * 2) - 1].skuId" placeholder="请输入SKU ID"
										  @blur="queryItemId((index * 2) - 1)"></el-input>
								<div class="el-form-item__error error-left" v-if="videoShopSKUList[(index * 2) - 1].isError">
									{{videoShopSKUList[(index * 2) - 1].errorMsg }}
								</div>
							</el-col>
						</el-form-item>
						<el-form-item label="商品区：">
							<el-col :span="10" style="margin-right:30px;">
								<img :src="editData.itemList[(index * 2) - 2].image"
									 style="width:185px;height:190px;margin-top:10px;border:0px;">
							</el-col>
							<el-col :span="10">
								<img :src="editData.itemList[(index * 2) - 1].image"
									 style="width:185px;height:190px;margin-top:10px;border:0px;">
							</el-col>
						</el-form-item>
						<el-form-item label="广告文案：">
							<el-col :span="10" style="margin-right:30px;">
								<el-input type="textarea" v-model="editData.itemList[(index * 2) - 2].description"
										  :autosize="{ minRows: 2, maxRows: 4}" placeholder=""></el-input>
							</el-col>
							<el-col :span="10">
								<el-input type="textarea" v-model="editData.itemList[(index * 2) - 1].description"
										  :autosize="{ minRows: 2, maxRows: 4}" placeholder=""></el-input>
							</el-col>
						</el-form-item>
					</div>
				</template>
				<!--模板2 店铺-->
				<template v-if="editData.pageTemplateId == 2">
					<el-form-item label="店铺ID：" prop="shopId">
						<el-input  placeholder="请输入店铺ID" style="width:400px;" v-model="editData.shopId"
								   :disabled="shopIdReadOnly" id="shopIdReadOnly" @blur="shopIdChange">
						</el-input>
					</el-form-item>
					<el-form-item label="店铺文案：" prop="description" :rules="rules.description">
						<el-input type="textarea" v-model="editData.description"
								  :autosize="{ minRows: 2, maxRows: 4}"
								  placeholder="请输入店铺文案" style="width:400px;">
						</el-input>
					</el-form-item>
					<p style="color: red; margin: 0 0 30px 125px;">SKU可输入的个数范围是4~10</p>
					<template>
						<el-form-item label="SKU ID：" style="width: 54%; display: inline-block; ">
							<el-col :span="25">
								<el-input v-model="editData.itemList[0].skuId" placeholder="请输入SKU ID" @blur="queryShopSKUId(0)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[0].isError">{{ videoShopSKUList[0].errorMsg }}</div>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight">
							<el-col :span="25">
								<el-input v-model="editData.itemList[1].skuId" placeholder="请输入SKU ID" @blur="queryShopSKUId(1)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[1].isError">{{ videoShopSKUList[1].errorMsg }}</div>
							</el-col>
						</el-form-item>
						<el-form-item label="商品名称：" style="width: 54%; display: inline-block; "
									  prop="itemList[0].name" :rules="rules.SKUname">
							<el-col :span="25">
								<el-input v-model="editData.itemList[0].name"  placeholder="请输入商品名称"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight"
									   prop="itemList[1].name" :rules="rules.SKUname">
							<el-col :span="25">
								<el-input v-model="editData.itemList[1].name"   placeholder="请输入商品名称"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item label="商品区：">
							<el-col :span="10" style="margin-right:30px;">
								<img :src="editData.itemList[0].image" style="width:185px;height:190px;margin-top:10px;border:0px;">
							</el-col>
							<el-col :span="10">
								<img :src="editData.itemList[1].image" style="width:185px;height:190px;margin-top:10px;border:0px;">
							</el-col>
						</el-form-item>
						<el-form-item label="广告文案："  style="width: 54%; display: inline-block; "
									  prop="itemList[0].description" :rules="rules.description">
							<el-col :span="30" >
								<el-input type="textarea" v-model="editData.itemList[0].description"
										  :autosize="{ minRows: 2, maxRows: 4}"
										  placeholder="请输入广告文案"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight"
									  prop="itemList[1].description" :rules="rules.description">
							<el-col :span="25">
								<el-input type="textarea" v-model="editData.itemList[1].description"
										  :autosize="{ minRows: 2, maxRows: 4}"
										  placeholder="请输入广告文案"></el-input>
							</el-col>
						</el-form-item>
					</template>
					<template>
						<el-form-item label="SKU ID：" style="width: 54%; display: inline-block; ">
							<el-col :span="25">
								<el-input v-model="editData.itemList[2].skuId" placeholder="请输入SKU ID" @blur="queryShopSKUId(2)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[2].isError">{{ videoShopSKUList[2].errorMsg }}</div>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight">
							<el-col :span="25">
								<el-input v-model="editData.itemList[3].skuId" placeholder="请输入SKU ID" @blur="queryShopSKUId(3)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[3].isError">{{ videoShopSKUList[3].errorMsg }}</div>
							</el-col>
						</el-form-item>
						<el-form-item label="商品名称：" style="width: 54%; display: inline-block; "
									  prop="itemList[2].name" :rules="rules.SKUname">
							<el-col :span="25">
								<el-input v-model="editData.itemList[2].name"  placeholder="请输入商品名称"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight"
									  prop="itemList[3].name" :rules="rules.SKUname">
							<el-col :span="25">
								<el-input v-model="editData.itemList[3].name"   placeholder="请输入商品名称"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item label="商品区：">
							<el-col :span="10" style="margin-right:30px;">
								<img :src="editData.itemList[2].image" style="width:185px;height:190px;margin-top:10px;border:0px;">
							</el-col>
							<el-col :span="10">
								<img :src="editData.itemList[3].image" style="width:185px;height:190px;margin-top:10px;border:0px;">
							</el-col>
						</el-form-item>
						<el-form-item label="广告文案："  style="width: 54%; display: inline-block; "
									  prop="itemList[2].description" :rules="rules.description">
							<el-col :span="30" >
								<el-input type="textarea" v-model="editData.itemList[2].description"
										  :autosize="{ minRows: 2, maxRows: 4}"
										  placeholder="请输入广告文案"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight"
									  prop="itemList[3].description" :rules="rules.description">
							<el-col :span="25">
								<el-input type="textarea" v-model="editData.itemList[3].description"
										  :autosize="{ minRows: 2, maxRows: 4}"
										  placeholder="请输入广告文案"></el-input>
							</el-col>
						</el-form-item>
					</template>
					<template v-if="shopSkuCount > 4">
						<el-form-item label="SKU ID：" style="width: 54%; display: inline-block; ">
							<el-col :span="25">
								<el-input v-model="editData.itemList[4].skuId" placeholder="请输入SKU ID" @blur="queryShopSKUId(4)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[4].isError">{{ videoShopSKUList[4].errorMsg }}</div>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight">
							<el-col :span="25">
								<el-input v-model="editData.itemList[5].skuId" placeholder="请输入SKU ID" @blur="queryShopSKUId(5)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[5].isError">{{ videoShopSKUList[5].errorMsg }}</div>
							</el-col>
						</el-form-item>
						<el-form-item label="商品名称：" style="width: 54%; display: inline-block; "
									  prop="itemList[4].name" :rules="rules.SKUname">
							<el-col :span="25">
								<el-input v-model="editData.itemList[4].name"  placeholder="请输入商品名称"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight"
									  prop="itemList[5].name" :rules="rules.SKUname">
							<el-col :span="25">
								<el-input v-model="editData.itemList[5].name"  placeholder="请输入商品名称"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item label="商品区：">
							<el-col :span="10" style="margin-right:30px;">
								<img :src="editData.itemList[4].image" style="width:185px;height:190px;margin-top:10px;border:0px;">
							</el-col>
							<el-col :span="10">
								<img :src="editData.itemList[5].image" style="width:185px;height:190px;margin-top:10px;border:0px;">
							</el-col>
						</el-form-item>
						<el-form-item label="广告文案："  style="width: 54%; display: inline-block; "
									  prop="itemList[4].description" :rules="rules.description">
							<el-col :span="30" >
								<el-input type="textarea" v-model="editData.itemList[4].description"
										  :autosize="{ minRows: 2, maxRows: 4}"
										  placeholder="请输入广告文案"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight"
									  prop="itemList[5].description" :rules="rules.description">
							<el-col :span="25">
								<el-input type="textarea" v-model="editData.itemList[5].description"
										  :autosize="{ minRows: 2, maxRows: 4}"
										  placeholder="请输入广告文案"></el-input>
							</el-col>
						</el-form-item>
					</template>
					<template v-if="shopSkuCount > 6">
						<el-form-item label="SKU ID：" style="width: 54%; display: inline-block; ">
							<el-col :span="25">
								<el-input v-model="editData.itemList[6].skuId" placeholder="请输入SKU ID" @blur="queryShopSKUId(6)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[6].isError">{{ videoShopSKUList[6].errorMsg }}</div>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight">
							<el-col :span="25">
								<el-input v-model="editData.itemList[7].skuId" placeholder="请输入SKU ID" @blur="queryShopSKUId(7)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[7].isError">{{ videoShopSKUList[7].errorMsg }}</div>
							</el-col>
						</el-form-item>
						<el-form-item label="商品名称：" style="width: 54%; display: inline-block; "
									  prop="itemList[6].name" :rules="rules.SKUname">
							<el-col :span="25">
								<el-input v-model="editData.itemList[6].name"  placeholder="请输入商品名称"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight"
									  prop="itemList[7].name" :rules="rules.SKUname">
							<el-col :span="25">
								<el-input v-model="editData.itemList[7].name"   placeholder="请输入商品名称"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item label="商品区：">
							<el-col :span="10" style="margin-right:30px;">
								<img :src="editData.itemList[6].image" style="width:185px;height:190px;margin-top:10px;border:0px;">
							</el-col>
							<el-col :span="10">
								<img :src="editData.itemList[7].image" style="width:185px;height:190px;margin-top:10px;border:0px;">
							</el-col>
						</el-form-item>
						<el-form-item label="广告文案："  style="width: 54%; display: inline-block; "
									  prop="itemList[6].description" :rules="rules.description">
							<el-col :span="30" >
								<el-input type="textarea" v-model="editData.itemList[6].description"
										  :autosize="{ minRows: 2, maxRows: 4}"
										  placeholder="请输入广告文案"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight"
									  prop="itemList[7].description" :rules="rules.description">
							<el-col :span="25">
								<el-input type="textarea" v-model="editData.itemList[7].description"
										  :autosize="{ minRows: 2, maxRows: 4}"
										  placeholder="请输入广告文案"></el-input>
							</el-col>
						</el-form-item>
					</template>
					<template v-if="shopSkuCount > 8">
						<el-form-item label="SKU ID：" style="width: 54%; display: inline-block; ">
							<el-col :span="25">
								<el-input v-model="editData.itemList[8].skuId" placeholder="请输入SKU ID" @blur="queryShopSKUId(8)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[8].isError">{{ videoShopSKUList[8].errorMsg }}</div>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight">
							<el-col :span="25">
								<el-input v-model="editData.itemList[9].skuId" placeholder="请输入SKU ID" @blur="queryShopSKUId(9)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[9].isError">{{ videoShopSKUList[9].errorMsg }}</div>
							</el-col>
						</el-form-item>
						<el-form-item label="商品名称：" style="width: 54%; display: inline-block; "
									  prop="itemList[8].name" :rules="rules.SKUname">
							<el-col :span="25">
								<el-input v-model="editData.itemList[8].name"  placeholder="请输入商品名称"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight"
									  prop="itemList[9].name" :rules="rules.SKUname">
							<el-col :span="25">
								<el-input v-model="editData.itemList[9].name"   placeholder="请输入商品名称"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item label="商品区：">
							<el-col :span="10" style="margin-right:30px;">
								<img :src="editData.itemList[8].image" style="width:185px;height:190px;margin-top:10px;border:0px;">
							</el-col>
							<el-col :span="10">
								<img :src="editData.itemList[9].image" style="width:185px;height:190px;margin-top:10px;border:0px;">
							</el-col>
						</el-form-item>
						<el-form-item label="广告文案："  style="width: 54%; display: inline-block; "
									  prop="itemList[8].description" :rules="rules.description">
							<el-col :span="30" >
								<el-input type="textarea" v-model="editData.itemList[8].description"
										  :autosize="{ minRows: 2, maxRows: 4}"
										  placeholder="请输入广告文案"></el-input>
							</el-col>
						</el-form-item>
						<el-form-item style="width: 54%;margin-left: -97px; display: inline-block;" class="skuIdRight"
									  prop="itemList[9].description" :rules="rules.description">
							<el-col :span="25">
								<el-input type="textarea" v-model="editData.itemList[9].description"
										  :autosize="{ minRows: 2, maxRows: 4}"
										  placeholder="请输入广告文案"></el-input>
							</el-col>
						</el-form-item>
					</template>
					<el-form-item >
						<div class="add-fn" v-show="shopSkuCount!==10"  @click="shopSkuCountAdd">
							<span class="btn btn-normal"></span>
						</div>
					</el-form-item>
				</template>
				<!--模板3 视频-->
				<template v-if="editData.pageTemplateId == 3">
					<el-form-item label="广告标题：" prop="title" :rules="rules.videoTitle" >
						<el-input style="width:400px;" v-model="editData.title" placeholder="请输入广告标题"></el-input>
					</el-form-item>
					<el-form-item label="上传视频：" >
						<div class="row-photo-s" style="width: 100%;">
							<div id="flashContent">
								<div id="flashPic">
								</div>
							</div>
							<input type="hidden" id="videoIds" v-model="editData.videoId">
							<div id="progressBox" style="display: none"><progress id="progress" value="0" max="100"></progress></div>
							<div class="el-form-item__error" id="videoIdsTip"></div>
						</div>
					</el-form-item>
					<el-form-item label="封面图片：">
						<el-radio-group v-model="editData.useDefaultImage" @change="videoImageTypeChange">
							<el-radio :label="0">默认图片</el-radio>
							<el-radio :label="1">自定义图片
								<span v-if="editData.useDefaultImage == 1" >
									<div @click="changeFile" style="display: inline-block;">
										 <input id="videoImage" type="file"
												accept="image/jpg,image/jpeg,image/png"
												style="width: 68px; height: 28px;visibility: hidden; position: absolute; opacity: 0"
												@change="videoImageChange">
										<div class="imgupload" style="display: inline-block; margin-left: 10px" >点击上传</div>
									</div>
                                </span>
							</el-radio>
						</el-radio-group>
						<div v-if="editData.useDefaultImage == 1">
							<div class="row-photo-s">
								<div class="el-upload__tip" style="width:100%">只能上传长宽比为2:1的图片文件</div>
								<div class="photo-add photo-add-b">
									<img :src="imgUrl" style="width: 400px; height: 247px;">
								</div>
							</div>
						</div>
					</el-form-item>
					<el-form-item label="视频描述：" prop="description">
						<el-input style="width:400px;" v-model="editData.description" type="textarea"
								  :autosize="{ minRows: 2, maxRows: 4}"
								  :rules = 'rules.description'
								  placeholder="请输入视频描述"></el-input>
					</el-form-item>
					<el-form-item label="推广内容：">
						<el-radio-group v-model="editData.promotionType" @change="promoteDetailChange">
							<el-radio class="radio" v-model="editData.promoteDetail" :label="0" style="display: none">店铺</el-radio>
							<el-radio class="radio" v-model="editData.promoteDetail" :label="2" style="margin-left: 0px">店铺</el-radio>
							<el-radio class="radio" v-model="editData.promoteDetail" :label="1">商品</el-radio>
						</el-radio-group>
					</el-form-item>
					<div v-if="editData.promotionType == 2">
						<el-form-item label="店铺LOGO：" v-show="videoShopLogoShow">
							<div>
								<div class="photo-add photo-add-b">
									<img v-model="editData.shopLogo" :src="editData.videoShopLogo"
										 style="width:400px; height:247px;">
								</div>
							</div>
						</el-form-item>
						<el-form-item label="店铺名称：" v-show="videoShopLogoShow">
							<el-input style="width:400px;" v-model="editData.videoShopName" type="text"
									  :disabled="shopIdReadOnly"></el-input>
						</el-form-item>
						<el-form-item label="店铺ID：" v-show="videoShopIdShow" prop="shopId">
							<el-input style="width:400px;" v-model="editData.shopId" type="text"
									  placeholder="请输入店铺ID" id="shopId" @blur="videoShopId"></el-input>
						</el-form-item>
						<el-form-item label="店铺文案：" prop="videoShopDescription" :rules="rules.videoDescription">
							<el-input style="width:400px;" v-model="editData.videoShopDescription" type="textarea"
									  :autosize="{ minRows: 2, maxRows: 4}"
									  placeholder="请输入店铺文案"></el-input>
						</el-form-item>
					</div>
					<div v-if="editData.promotionType == 1">
						<el-row>
							<el-form-item label="商品SKU：" prop="">
								<el-input style="width:400px;" v-model="editData.itemList[0].skuId" type="text"
										  placeholder="请输入商品SKU ID" @blur="queryVideoShopSKUId(0)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[0].isError">
									{{videoShopSKUList[0].errorMsg }}
								</div>
							</el-form-item>
							<el-form-item label="商品名称：" prop="itemList[0].name" :rules="rules.SKUname">
								<el-input style="width:400px;" v-model="editData.itemList[0].name" type="text"
										  placeholder="请输入商品名称"></el-input>
							</el-form-item>
							<el-form-item label="商品图片：" prop="" >
									<div class="photo-add photo-add-b">
										<img :src="editData.itemList[0].image" style="width: 400px; height: 274px;">
									</div>

									<!--<swiper :options="swiperOptions"  ref="mySwiperBig" >
										<swiper-slide v-for='(item, index) in editData.itemList[0].image' :key="item.index" >
											<img :src="item"/>
										</swiper-slide>
										<div class="swiper-button-prev"></div>
										<div class="swiper-button-next"></div>
									</swiper>-->

								<!--<div class="photoBox">
									<el-button class="scrollBtn" @click="photoNext(0)" v-show=" editData.itemList[0].image.length > 0 "
									           :disabled="rightBtnDisabled"><</el-button>
									<div class="photoContent">
										<ul :style="{width: editData.itemList[0].image.length * 85 + 'px'}">
											<li v-for="(item,index) in editData.itemList[0].image" class="photoContentLi">
												<img :src="item">
											</li>
										</ul>
									</div>
									<el-button class="scrollBtn" @click="photoPrev(0)" v-show=" editData.itemList[0].image.length > 0 "
											   :disabled="leftBtnDisabled"
											   style="margin-left: 1%"><</el-button>
								</div>-->
							</el-form-item>
							<el-form-item label="商品文案：" prop="itemList[0].description" :rules="rules.SKUname">
								<el-input style="width:400px;" v-model="editData.itemList[0].description" type="textarea"
										  placeholder="请输入商品文案"
										  :autosize="{ minRows: 2, maxRows: 4}">
								</el-input>
							</el-form-item>
						</el-row>
						<el-row v-show='videoDesCount >= 2'>
							<el-form-item  label="商品SKU：" prop="">
								<el-input style="width:400px;" v-model="editData.itemList[1].skuId" type="text"
										  placeholder="请输入商品SKU ID" @blur="queryVideoShopSKUId(1)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[1].isError">
									{{videoShopSKUList[1].errorMsg }}
								</div>
							</el-form-item>
							<el-form-item label="商品名称：" prop="itemList[1].name" :rules="rules.SKUname">
								<el-input style="width:400px;" v-model="editData.itemList[1].name" type="text"
										  placeholder="请输入商品名称"></el-input>
							</el-form-item>
							<el-form-item label="商品图片:" prop="">
								<div class="row-photo-s">
									<div class="photo-add photo-add-b">
										<img :src="editData.itemList[1].image" style="width: 400px; height: 274px;">
									</div>
								</div>
							</el-form-item>
							<el-form-item label="商品文案：" prop="itemList[1].description" :rules="rules.SKUname">
								<el-input style="width:400px;" v-model="editData.itemList[1].description" type="textarea"
										  placeholder="请输入商品文案"
										  :autosize="{ minRows: 2, maxRows: 4}">
								</el-input>
							</el-form-item>
						</el-row>
						<el-row v-show='videoDesCount >= 3'>
							<el-form-item label="商品SKU：" prop="">
								<el-input style="width:400px;" v-model="editData.itemList[2].skuId" type="text"
										  placeholder="请输入商品SKU ID" @blur="queryVideoShopSKUId(2)"></el-input>
								<div class="el-form-item__error" v-if="videoShopSKUList[2].isError">{{
									videoShopSKUList[2].errorMsg }}
								</div>
							</el-form-item>
							<el-form-item label="商品名称：" prop="itemList[2].name"  :rules="rules.SKUname">
								<el-input style="width:400px;" v-model="editData.itemList[2].name" type="text" :maxlength="60"
										  placeholder="请输入商品名称"></el-input>
							</el-form-item>
							<el-form-item label="商品图片：" prop="">
								<div class="row-photo-s">
									<div class="photo-add photo-add-b">
										<img :src="editData.itemList[2].image" style="width: 400px; height: 274px;">
									</div>
								</div>
							</el-form-item>
							<el-form-item label="商品文案：" prop="itemList[2].description" :rules="rules.SKUname">
								<el-input style="width:400px;" v-model="editData.itemList[2].description" type="textarea"
										  placeholder="请输入商品文案"
										  :autosize="{ minRows: 2, maxRows: 4}">
								</el-input>
							</el-form-item>
						</el-row>
						<el-form-item >
							<div class="add-fn" v-show="videoDesCount !== 3" @click="addVideoDesCount">
								<span class="btn btn-normal"></span>
							</div>
						</el-form-item>
					</div>
				</template>
				<el-form-item label="页面背景色：" v-if="editData.pageTemplateId == 1">
					<el-input style="width:400px;" :readonly="true" v-model="selectColor" @focus="handlePalette"></el-input>
					<sketch-picker v-model="colors" @input="onChange" v-if="showChildComponents.showPalette" :showPalette="showChildComponents.showPalette"></sketch-picker>
				</el-form-item>
				<el-form-item label="分享卡图片：" >
					<div class="detail-row"  style="width: 146px;margin: 0 20px 0 0;float: left;">
						<div class="row-photo-s" @click="changeShareFile">
							<input id="sharePicture" type="file" style="width: 146px;height: 100px;display:block;position:absolute;visibility: hidden;" @change="sharePictureChange" accept="image/jpg,image/jpeg,image/png">
							<div class="photo-add photo-add-b"><img :src="cardImg">
							</div>
							<div class="el-upload__tip" style="color:red;text-align:center;width:150px;">建议上传150*150的图片</div>
						</div>
					</div>
				</el-form-item>
				<el-form-item label="分享卡名称：">
					<el-input style="width:400px;" v-model="editData.cardTitle"></el-input>
				</el-form-item>
				<el-form-item label="分享卡文案：">
					<el-input type="textarea" v-model="editData.cardDescription" :autosize="{ minRows: 2, maxRows: 4}" placeholder="" style="width:400px;"></el-input>
				</el-form-item>
			</el-form>
		</div>
	</div>
</template>
<script>
import Vue from 'vue';
import { swiper, swiperSlide } from 'vue-awesome-swiper';
import apiConfig from '../../../../config/api.config.js';
import store from 'store';
import {
	copyObj,
	objType,
	formatDate,
	mixin,
	debounce,
	getImgSize,
	isEmptyObj,
	limitLen
} from '../../../../utils/common';
import Event from 'event';
import Http from 'http';
import { Sketch } from 'vue-color';
let defaultProps = {
	hex: '#194d33',
	hsl: {
		h: 150,
		s: 0.5,
		l: 0.2,
		a: 1
	},
	hsv: {
		h: 150,
		s: 0.66,
		v: 0.30,
		a: 1
	},
	rgba: {
		r: 25,
		g: 77,
		b: 51,
		a: 1
	},
	a: 1
};
require('../../../../utils/uploadVideo.js');
export default {
	name: 'app-page-edit',
	props: ['pageId'],
	data() {
		return {
		    isClickBtn:false,
			rightBtnDisabled:true,
			leftBtnDisabled:false,
		    videoShopData:[],
			videoShopLogoShow:true,
			videoShopIdShow:false,
			videoDesCount: 1,
			videoShopSKUList:[
				{skuId: '', isError: false, errorMsg: ''},
				{skuId: '', isError: false, errorMsg: ''},
				{skuId: '', isError: false, errorMsg: ''},
				{skuId: '', isError: false, errorMsg: ''},
				{skuId: '', isError: false, errorMsg: ''},
				{skuId: '', isError: false, errorMsg: ''},
				{skuId: '', isError: false, errorMsg: ''},
				{skuId: '', isError: false, errorMsg: ''},
				{skuId: '', isError: false, errorMsg: ''},
				{skuId: '', isError: false, errorMsg: ''}
			],
			shopIdReadOnly:false,
			showChildComponents: {
				showPalette: false
			},
			imageFile: null,
			formData: {
				itemList: [],
			},
			editData: {
				videoId: '0',
				itemList: [],
			},
			imgUrl: '',
			cardImg:'',
			itemList: [],
			uploadUrl: `${$CONFIG['domain']}api/image/upload`,
			fileType: ".jpg, .png, .jpeg, |images/*",
			colors: defaultProps,
			selectColor: "",
			shopSkuCount:4,
			rules:{
		        'videoTitle':[{validator: function(rule, value, callback) {
					if(limitLen(value, 30)){
						callback();
					}else{
						callback(new Error('最大长度不超过15个汉字'));
					}
				}, trigger: 'blur' }],
		        'videoDescription':[{validator: function(rule, value, callback) {
					if(limitLen(value, 100)){
						callback();
					}else{
						callback(new Error('最大长度不超过50个汉字'));
					}
				}, trigger: 'blur' }],
		        'description':[{validator: function(rule, value, callback) {
					if(limitLen(value, 200)){
						callback();
					}else{
						callback(new Error('最大长度不超过100个汉字'));
					}
					}, trigger: 'blur' }],
				'SKUname':[{validator: function(rule, value, callback) {
					if(limitLen(value, 60)){
						callback();
					}else{
						callback(new Error('最大长度不超过30个汉字'));
					}
					}, trigger: 'blur'}],
			}

		};
	},
	computed: {
		drawerData: () => store.state.drawerCtrl.config, // from store
		drawerCtrlAction: () => store.state.drawerCtrl.action
	},
	components: {
		'sketch-picker': Sketch
	},
	created() {
	    let itemList = {'itemList':[]};
		this.editData = copyObj(mixin(this.drawerData,itemList));
		this.cardImg = this.editData.cardImage;
		this.imgUrl = this.editData.image;
		this.selectColor = this.editData.backgroundColor;
		window.UploadVideoId = '0';
		for (var i =0; i < 10; i++) {
			let item = {skuId:'', image:[], productId: '', description:'' ,name:''};
			this.editData.itemList.push(item);
		}
		if (store.state.drawerCtrl.action === 'modify') {
			if(this.editData.pageTemplateId == 3){ //视频
				if(this.editData.promotionType == 2 ){ //店铺
					this.editData.shopId = this.editData.data[0].shopId
					this.editData.videoShopLogo = this.editData.data[0].image;
					this.editData.videoShopName = this.editData.data[0].name;
					this.editData.videoShopDescription = this.editData.data[0].description;
					console.log(this.editData.shopId);
					Http.get("/api/page/shop", {
						hideLoading: true,
					})
						.then((res) => {
							if (res.data.code === 200 && !isEmptyObj(res.data.data)) {
								this.editData.shopId = res.data.data.id;
								this.videoShopLogoShow = true;
								this.videoShopIdShow = false;
								this.shopIdReadOnly = true;
							} else if(this.editData.shopId  !== ""){
								this.videoShopLogoShow = true;
								this.videoShopIdShow = true;
								this.shopIdReadOnly = true;
							}else {
								this.videoShopLogoShow = false;
								this.videoShopIdShow = true;
								this.shopIdReadOnly = true;
							}
						})
						.catch(function (error) {
							console.log(error);
						});
				}else if(this.editData.promotionType == 1){
					this.videoDesCount = this.editData.data.length;
					this.editData.data.map((item,index)=>{
					    this.editData.itemList[index].skuId = this.editData.data[index].skuId;
					    this.editData.itemList[index].image = this.editData.data[index].image;
					    this.editData.itemList[index].productId = this.editData.data[index].productId;
					    this.editData.itemList[index].description = this.editData.data[index].description;
					    this.editData.itemList[index].name = this.editData.data[index].name;
					})
				}
			}
			if(this.editData.pageTemplateId == 2){
				Http.get("/api/page/shop", {
					hideLoading: true,
				}).then(res => {
					if (res.data.code === 200 && !isEmptyObj(res.data.data)) {
						this.shopIdReadOnly = true;
					}

				}).catch(function (error) {
					console.log(error);
				});
				this.editData.data.map((item,index)=>{
					this.editData.itemList[index].skuId = this.editData.data[index].skuId;
					this.editData.itemList[index].image = this.editData.data[index].image;
					this.editData.itemList[index].productId = this.editData.data[index].productId;
					this.editData.itemList[index].description = this.editData.data[index].description;
					this.editData.itemList[index].name = this.editData.data[index].name;
				})
				this.shopSkuCount = this.editData.data.length;

			}
			if(this.editData.pageTemplateId == 1 || this.editData.pageTemplateId == 4){
				Http.get("/api/page/shop", {
					hideLoading: true,
				}).then(res => {
					if (res.data.code === 200 && !isEmptyObj(res.data.data)) {
						this.editData.shopId = res.data.data.id;
					}
				}).catch(function (error) {
                     console.log(error);
				});
				this.editData.data.map((item,index)=>{
					this.editData.itemList[index].skuId = this.editData.data[index].skuId;
					this.editData.itemList[index].image = this.editData.data[index].image;
					this.editData.itemList[index].productId = this.editData.data[index].productId;
					this.editData.itemList[index].description = this.editData.data[index].description;
					this.editData.itemList[index].name = this.editData.data[index].name;
				})
			}
		}
		this.getShopId();
	},
	mounted() {
		Event.$off('page-save');
		Event.$on('page-save', (avg) =>{
		    let RES = '';
		    if(avg.publish == 'publish'){
				this.$refs.editData.validate((result) => {
					if (!result) {
					    RES = result;
					};
				});
			};
		    if(RES !== "" && RES == false)  return false;
			if (avg.pId !== '' ) {
				//修改
				if (this.editData.pageTemplateId == 3) {
					if (this.editData.promotionType == 2) { //视频里面有店铺 2=>店铺  1=>商品
						this.editData.itemList = [{
							'image': this.editData.videoShopLogo,
							'name': this.editData.videoShopName,
							'shopId': this.editData.shopId,
							'description': this.editData.videoShopDescription,
						}]
					}
					if (window.UploadVideoId !== '0') {
						this.editData.videoId = window.UploadVideoId;
					}
				}
				;
				let params = {
					videoId: this.editData.videoId, //视频ID
					promotionType: this.editData.promotionType,//推广内容
					useDefaultImage: this.editData.useDefaultImage, //0 默认 1自定义
					pageTemplateId: this.editData.pageTemplateId,
					shopId: this.editData.shopId,
					title: this.editData.title ? this.editData.title : '',
					name: this.editData.name,
					image: this.imgUrl ? this.imgUrl : '',
					description: this.editData.description ? this.editData.description : '',
					pageTemplatePlatform: this.editData.platform ? this.editData.platform : '1',
					data: this.editData.itemList,
					backgroundColor: this.selectColor ? this.selectColor : '',
					cardTitle: this.editData.cardTitle ? this.editData.cardTitle : '',
					cardImage: this.cardImg ? this.cardImg : '',
					cardDescription: this.editData.cardDescription ? this.editData.cardDescription : '',
					pageId: this.editData.pageId ? this.editData.pageId : avg.pId
				};
				Http.put("/api/page",
					params
				)
					.then((res) => {
						if (!res.data.iserror) {
							Event.$emit('page-save-result', {
								error: 0,
								res: mixin(res.data, {videoId: this.editData.videoId})
							});
						} else {
							Event.$emit('page-save-result', {
								error: 1,
								res: res.data
							});
						}
					})
					.catch((e) => {
						Event.$emit('page-save-result', {
							error: 1,
							res: res.data
						});
					});
			} else {
				//新建
				if (this.editData.pageTemplateId == 3) {  //视频
					if (this.editData.promotionType == 2) { //视频里面有店铺 2=>店铺  1=>商品
						this.editData.itemList = [{
							'image': this.editData.videoShopLogo,
							'name': this.editData.videoShopName,
							'shopId': this.editData.shopId,
							'description': this.editData.videoShopDescription,
						}]
					}
					if (window.UploadVideoId !== '0') {
						this.editData.videoId = window.UploadVideoId;
					} else {
						this.editData.videoId = '0'
					}
				}
				;
				let params = {
					//视频
					videoId: this.editData.videoId, //视频ID
					promotionType: this.editData.promotionType,//推广内容
					useDefaultImage: this.editData.useDefaultImage, //0 默认 1自定义
					pageTemplateId: this.editData.pageTemplateId,
					shopId: this.editData.shopId,
					name: this.editData.name,
					pageTemplatePlatform: this.editData.platform ? this.editData.platform : '1',
					title: this.editData.title ? this.editData.title : '',
					image: this.imgUrl ? this.imgUrl : '',
					description: this.editData.description ? this.editData.description : '',
					data: this.editData.itemList,
					backgroundColor: this.selectColor ? this.selectColor : '',
					cardTitle: this.editData.cardTitle ? this.editData.cardTitle : '',
					cardImage: this.cardImg ? this.cardImg : '',
					cardDescription: this.editData.cardDescription ? this.editData.cardDescription : ''
				};
				Http.post("/api/page",
					params
				)
					.then((res) => {
						if (!res.data.iserror) {
							Event.$emit('page-save-result', {
								error: 0,
								res: mixin(res.data, {
									videoId: this.editData.videoId,
									pageTemplateId: this.editData.pageTemplateId
								})
							});
						} else {
							Event.$emit('page-save-result', {
								error: 1,
								res: res.data

							});
						}
					})
					.catch((e) => {
						Event.$emit('page-save-result', {
							error: 1,
							res: res.data
						});
					});
			}
		});
		Event.$off('page-publish');
			Event.$on('page-publish', (argv) => {
				Http.put("/api/page/publish", {
						pageId: argv.res.data.pageId
					}
				)
				.then((res) => {
					if (!res.data.iserror) {
						Event.$emit('page-publish-result', {
							error: 0,
							res: res.data
						});
					} else {
						Event.$emit('page-publish-result', {
							error: 1,
							res: res.data
						});
					}
				})
				.catch((e) => {
					Event.$emit('page-publish-result', {
						error: 1,
						res: res.data
					});
				});
		});
		//视频渲染
		if(this.editData.pageTemplateId == 3){
			let flashPic = document.getElementById('flashPic');
			let pageHost = ((document.location.protocol == "https:") ? "https://" : "http://");
			flashPic.innerHTML = "<a href='http://www.adobe.com/go/getflashplayer'><img src='"
				+ pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' target='_blank' /></a>";
			uploadVideo();
		}
	},
	methods: {
		photoPrev(index){
		    if(this.isClickBtn) return false;
			this.isClickBtn = true;
			let now = -7 * (50 + 5);
		    let len = this.editData.itemList[index].image.length;
		    let oUl = document.getElementsByClassName('photoContent')[index].getElementsByTagName('ul')[0];
			let n = Math.floor((len * (50 + 5) + oUl.offsetLeft) / 50);
			if (n <= 7) {
				this.move(oUl, 'left', 0,index);
			} else {
				this.move(oUl, 'left', oUl.offsetLeft + now,index);
			}
		},
		photoNext(index){
			if(this.isClickBtn) return false;
			this.isClickBtn = true;
			let len  = this.editData.itemList[index].image.length;
			let oUl  = document.getElementsByClassName('photoContent')[index].getElementsByTagName('ul')[0];
			let now1 = -Math.floor((len / 7) ) * 7 * (50 + 5);
			let now  = 7 * (50 + 5);
			if (oUl.offsetLeft >= 0) {
				this.move(oUl, 'left', now1,index);
			} else {
				this.move(oUl, 'left', oUl.offsetLeft + now,index);
			}
		},
	    move(obj, attr, iTarget,index){
			clearInterval(obj.timer)
			let vm = this;
			let len  = this.editData.itemList[index].image.length;
			let oUl  = document.getElementsByClassName('photoContent')[index].getElementsByTagName('ul')[0];
			let now1 = -Math.floor((len / 7) ) * 7 * (50 + 5);
			obj.timer = setInterval(function () {
				let cur = 0;
				cur = parseInt(vm.getStyle(obj, attr));
				let speed = (iTarget - cur) / 6;
				speed = speed > 0 ? Math.ceil(speed) : Math.floor(speed);
				if (iTarget == cur) {
					clearInterval(obj.timer);
					vm.isClickBtn = false;
					if(oUl.offsetLeft == 0){
						vm.rightBtnDisabled = true;
					}else {
						vm.rightBtnDisabled = false;
					}
					if( oUl.offsetLeft == now1){
						vm.leftBtnDisabled = true;
					}else{
						vm.leftBtnDisabled = false;
					}
				} else {
					obj.style[attr] = cur + speed + 'px';
				}
			}, 20);


		},
		getStyle(obj, name) {
			if (obj.currentStyle) {
				return obj.currentStyle[name];
			}
			else {
				return getComputedStyle(obj, false)[name];
			}
		},
		videoShopId(){
		    if(this.editData.shopId == '') {
				this.videoShopLogoShow = false;
				this.videoShopIdShow = true;
				return false;
			};
			Http.get("/api/page/shop", {
				params: {
					shopId: this.editData.shopId
				}
			}).then(res=> {
				if (res.data.code === 200 && !isEmptyObj(res.data.data)) {
					this.editData.videoShopName = res.data.data.name;
					this.editData.videoShopLogo = res.data.data.icon;
					this.editData.videoShopDescription = res.data.data.description;
					this.shopIdReadOnly = true;
					this.videoShopLogoShow = true;
					this.videoShopIdShow = true;
				} else {
					this.editData.videoShopName = '';
					this.editData.videoShopLogo = '';
					this.editData.shopId = '';
					this.videoShopLogoShow = false;
					this.videoShopIdShow = true;
					this.shopIdReadOnly = false;
					this.$message({
						message: '店铺ID填写错误!',
						type: 'error'
					});
					return false;
				}
			}).catch(function (error) {
			    console.log(error);
			});
		},
		queryVideoShopSKUId(index){
			if (this.editData.itemList[index].skuId === "" ) {
				this.editData.itemList[index].description = '';
				this.editData.itemList[index].image = '';
				this.editData.itemList[index].productId = '';
				this.editData.itemList[index].name = '';
				return;
			} else{
				let currentSku = this.editData.itemList.find((item,i) => {
					return i !== index && item.skuId === this.editData.itemList[index].skuId;
				});
				if(currentSku !== undefined){
					this.videoShopSKUList[index].isError = true;
					this.videoShopSKUList[index].errorMsg = '请不要输入重复商品';
					this.editData.itemList[index].skuId = '';
					this.editData.itemList[index].description = '';
					this.editData.itemList[index].image = '';
					this.editData.itemList[index].productId = '';
					this.editData.itemList[index].name = '';
					setTimeout(()=>{
						this.videoShopSKUList[index].errorMsg = '';
					},3000)
					return false;
				}
				Http.get('/api/material/sku', {
					params: {
						skuId: this.editData.itemList[index].skuId
					}
				})
					.then((res) => {
						if (res.data.data.skuId == null  &&  res.data.data.productId == null ) {
							this.editData.itemList[index].skuId = '';
							this.$message({
								message: '未找到该商品!',
								type: 'error'
							});
							return false;
						} else if(this.editData.shopId !== "" && res.data.data.shopId !== this.editData.shopId ) {
							this.editData.itemList[index].skuId = ''
							this.editData.itemList[index].description = '';
							this.editData.itemList[index].image = '';
							this.editData.itemList[index].productId = '';
							this.$message({
								message: '请填写店铺内的SKU ID!',
								type: 'warning'
							});
							return false;
						}else{
							this.editData.itemList[index].description = res.data.data.description;
							this.editData.itemList[index].image = res.data.data.images[0];
							this.editData.itemList[index].productId = res.data.data.productId;
							this.editData.itemList[index].name = res.data.data.name;
						}
					});
			}
		},
		videoImageTypeChange(){
		    this.imgUrl = '';
		},
		promoteDetailChange(obj){
			this.videoDesCount = 1;
			this.editData.itemList = [
				{'skuId':'','image':'','description':'','name':''},
				{'skuId':'','image':'','description':'','name':''},
				{'skuId':'','image':'','description':'','name':''},
				{'skuId':'','image':'','description':'','name':''},
				{'skuId':'','image':'','description':'','name':''},
				{'skuId':'','image':'','description':'','name':''},
				{'skuId':'','image':'','description':'','name':''},
				{'skuId':'','image':'','description':'','name':''},
				{'skuId':'','image':'','description':'','name':''},
				{'skuId':'','image':'','description':'','name':''},
			];
//			this.editData.videoShopDescription = '';
//			this.editData.shopId = '';
			  //2=>店铺 1=>商品
			Http.get("/api/page/shop", {
			    hideLoading:true
			})
				.then(res => {
					if (res.data.code === 200 && !isEmptyObj(res.data.data)) {
						this.editData.videoShopName = res.data.data.name;
						this.editData.videoShopLogo = res.data.data.icon;
						this.editData.videoShopDescription = res.data.data.description;
						this.editData.shopId = res.data.data.id;
						this.videoShopLogoShow = true;
						this.videoShopIdShow = false;
						this.shopIdReadOnly = true;
					} else {
						this.editData.videoShopName = '';
						this.editData.videoShopLogo = '';
						this.editData.videoShopDescription = '';
						this.editData.shopId = '';
						this.videoShopLogoShow = false;
						this.videoShopIdShow = true;
						this.shopIdReadOnly = false;
					}
				})
				.catch(function (error) {
                   console.log(error);
				});

		},
		addVideoDesCount(){
			this.videoDesCount = this.videoDesCount === 3 ? this.videoDesCount : this.videoDesCount + 1;
		},
		changeFile() {
			let input = document.getElementById("videoImage");
			input.click();
		},
		videoImageChange(eve){
			let files= eve.target.files;
			this.loadVideoImage(files[0]);
		},
		loadVideoImage(file){
			if (file.size > 500 *1024) {
				this.$message({
					message: '您上传的图片太大，请重新上传！',
					type: 'warning'
				});
				return false;
			}
			let that = this;
			let imageData = new FormData();
			let reader = new FileReader();
			reader.onload = function(e) {
				let data = e.target.result;
				//加载图片获取图片真实宽度和高度
				let image = new Image();
				image.onload=function(){
					let width = image.width;
					let height = image.height;
					let scale = width / height;
					console.log(scale);
					if(scale !== 2) {
						that.$message({
							message: '您上传的图片比例应该为2:1，请重新上传！',
							type: 'warning'
						});
					} else {
						imageData.append('file', file);
						that.videoImageUpload(imageData);
					}
				};
				image.src= data;
			};
			reader.readAsDataURL(file);
		},
		videoImageUpload(data){
			Http.post(this.uploadUrl, data)
				.then((res) => {
					if (res.data.code === 200) {
						this.imgUrl = res.data.data.imageUrl;
					}
				})
				.catch((error) => {
					console.log(error);
				});
		},
	    //店铺
		shopIdChange(){
		    if(this.editData.shopId == '') return;
			Http.get("/api/page/shop", {
				params: {
					shopId: this.editData.shopId
				}
			}).then(res=> {
				if (res.data.code === 200 && !isEmptyObj(res.data.data)) {
					this.editData.title = res.data.data.name;
					this.imgUrl = res.data.data.icon;
					this.editData.shopId = res.data.data.id;
					this.editData.description = res.data.data.description;
					this.videoShopLogoShow = true;
					this.videoShopIdShow = false;
				} else {
					this.editData.videoShopName = '';
					this.editData.videoShopLogo = '';
					this.editData.shopId = '';
					this.videoShopLogoShow = false;
					this.videoShopIdShow = true;
					this.shopIdReadOnly = false;
					this.$message({
						message: '店铺ID填写错误!',
						type: 'error'
					});
					return false;
				}
			})
				.catch(function(error) {

				});
		},
		shopSkuCountAdd(){
			this.shopSkuCount = this.shopSkuCount === 10 ? this.shopSkuCount : this.shopSkuCount + 2;
		},
		getShopId(){
		    console.log();
			if(store.state.drawerCtrl.action === 'new'){
				Http.get("/api/page/shop", {
					hideLoading: true,
				})
					.then(res=> {
						if (res.data.code === 200 && !isEmptyObj(res.data.data)) {
							this.editData.shopId = res.data.data.id;
							if(this.editData.pageTemplateId == 2){
								this.shopIdReadOnly = true;
								this.editData.title = res.data.data.name;
								this.imgUrl = res.data.data.icon;
								this.editData.description = res.data.data.description;
							}

						}
					})
					.catch(function(error) {

					});
			}
		},
		queryShopSKUId(index){
			if (this.editData.itemList[index].skuId === "" ) {
				this.editData.itemList[index].description = '';
				this.editData.itemList[index].name = '';
				this.editData.itemList[index].image = '';
				this.editData.itemList[index].productId = '';
				return;
			} else if(this.editData.shopId === "" || this.editData.shopId == undefined ){
				this.editData.itemList[index].skuId = '';
				this.$message({
					message: '请先填写店铺ID!',
					type: 'error'
				});
				return false;
			} else{
				let currentSku = this.editData.itemList.find((item,i) => {
				    return i !== index && item.skuId === this.editData.itemList[index].skuId;
				});

				if(currentSku !== undefined){
					this.videoShopSKUList[index].isError = true;
					this.videoShopSKUList[index].errorMsg = '请不要输入重复商品';
					this.editData.itemList[index].skuId = '';
					this.editData.itemList[index].description = '';
					this.editData.itemList[index].name = '';
					this.editData.itemList[index].image = '';
					this.editData.itemList[index].productId = '';
					setTimeout(()=>{
						this.videoShopSKUList[index].errorMsg = '';
					},3000)
					return false;
				};
				Http.get('/api/material/sku', {
					params: {
						skuId: this.editData.itemList[index].skuId
					}
				})
					.then((res) => {
						if (res.data.data.skuId == null  &&  res.data.data.productId == null ) {
							this.editData.itemList[index].skuId = '';
							this.$message({
								message: '未找到该商品!',
								type: 'error'
							});
							return false;
						} else if(res.data.data.shopId !== this.editData.shopId){
							this.editData.itemList[index].skuId = ''
							this.editData.itemList[index].description = '';
							this.editData.itemList[index].name = '';
							this.editData.itemList[index].image= '';
							this.editData.itemList[index].productId= '';
							this.$message({
								message: '请填写店铺内的SKU ID!',
								type: 'warning'
							});
							return false;
						}else{
							this.editData.itemList[index].description = res.data.data.description;
							this.editData.itemList[index].name = res.data.data.name;
							this.editData.itemList[index].image = res.data.data.images[0];
							this.editData.itemList[index].productId = res.data.data.productId;
						}
					});
			}
		},
		changeTopFile() {
			let input = document.getElementById("topPicture");
			input.click();
		},
		topPictureChange(eve) {
			let files= eve.target.files;
			this.loadTopFile(files[0]);
		},
		loadTopFile(file) {
			if (file.size > 500 *1024) {
				this.$message({
					message: '您上传的图片太大，请重新上传！',
					type: 'warning'
				});
				return false;
			}
			let that = this;
			let imageData = new FormData();
			let reader = new FileReader();
			reader.onload = function(e) {
				let data = e.target.result;
				//加载图片获取图片真实宽度和高度
				let image = new Image();
				image.onload=function(){
					let width = image.width;
					let height = image.height;
					let scale = width / height;
					console.log(scale);
					if(scale !== 2) {
						that.$message({
							message: '您上传的图片比例应该为2:1，请重新上传！',
							type: 'warning'
						});
					} else {
						imageData.append('file', file);
						that.imageTopUpload(imageData);
					}
				};
				image.src= data;
			};
		   reader.readAsDataURL(file);
		},
		imageTopUpload(data) {
			Http.post(this.uploadUrl, data)
			.then((res) => {
				if (res.data.code === 200) {
					this.imgUrl = res.data.data.imageUrl;
				}
			})
			.catch((error) => {
				console.log(error);
			});
		},

		handlePalette() {
			this.showChildComponents.showPalette = true;
		},
		onChange(val) {
			this.selectColor = val.hex;
			console.log('this.selectColor: ' + this.selectColor);
			console.log('val.hex: ' + val.hex);
		},
		changeShareFile(){
			let input = document.getElementById("sharePicture");
			input.click();
		},
		sharePictureChange(eve) {
			let files= eve.target.files;
			this.loadShareImage(files[0]);

		},
		loadShareImage(file) {
			if (file.size > 15 *1024) {
				this.$message({
					message: '您上传的图片太大，请重新上传！',
					type: 'warning'
				});
				return false;
			}
			let that = this;
			let imageData = new FormData();
			let reader = new FileReader();
			reader.onload = function(e) {
				let data = e.target.result;
				//加载图片获取图片真实宽度和高度
				let image = new Image();
				image.onload=function(){
					let width = image.width;
					let height = image.height;
					if(width !== height) {
						that.$message({
							message: '您上传的图片比例应该为150*150，请重新上传！',
							type: 'warning'
						});
					} else {
						imageData.append('file', file);
						that.shareImageUpload(imageData);
					}
				};
				image.src= data;
			};
		   reader.readAsDataURL(file);
		},
		shareImageUpload(data) {
			Http.post(this.uploadUrl, data)
			.then((res) => {
				if (res.data.code === 200) {
					this.cardImg = res.data.data.imageUrl;
				}
			})
			.catch((error) => {
				console.log(error);
			});
		},
		queryItemId(index) {
			if (this.editData.itemList[index].skuId === "" || this.editData.itemList[index].skuId.length === 0) {
				this.editData.itemList[index].description = '';
				this.editData.itemList[index].image = '';
				this.editData.itemList[index].productId = '';
				this.editData.itemList[index].name = '';
				return;
			} else {
				let currentSku = this.editData.itemList.find((item,i) => {
					return i !== index && item.skuId === this.editData.itemList[index].skuId;
				});

				if(currentSku !== undefined){
					this.videoShopSKUList[index].isError = true;
					this.videoShopSKUList[index].errorMsg = '请不要输入重复商品';
					this.editData.itemList[index].skuId = '';
					this.editData.itemList[index].description = '';
					this.editData.itemList[index].image = '';
					this.editData.itemList[index].productId = '';
					this.editData.itemList[index].name = '';
					setTimeout(()=>{
						this.videoShopSKUList[index].errorMsg = '';
					},2000);
					return false;
				}
				Http.get('/api/material/sku', {
					params: {
						skuId: this.editData.itemList[index].skuId
					}
				})
				.then((res) => {
					if ( res.data.data.skuId == null && res.data.data.productId == null) {
						this.editData.itemList[index].skuId = '';
						this.editData.itemList[index].description = '';
						this.editData.itemList[index].image= '';
						this.editData.itemList[index].productId= '';
						this.editData.itemList[index].name= '';
						this.$message({
							message: '未找到该商品!',
							type: 'error'
						});
						return false;
					} else if(this.editData.shopId !== "" && res.data.data.shopId !== this.editData.shopId ){
						this.editData.itemList[index].skuId = ''
						this.editData.itemList[index].description = '';
						this.editData.itemList[index].image = '';
						this.editData.itemList[index].productId = '';
						this.$message({
							message: '请填写店铺内的SKU ID!',
							type: 'warning'
						});
						return false;
					}else{
						this.editData.itemList[index].description = res.data.data.description;
						this.editData.itemList[index].image = res.data.data.images[0];
						this.editData.itemList[index].productId = res.data.data.productId;
					}
				});
			}
		},
		selectImg(index) {
			this.formData.image = this.allImgList[index].url;
			this.allImgList.forEach((item) => {
				item.checked = false;
			});
			this.allImgList[index].checked = true;
		},
		upSuccess(response, file, fileList){
			if (response.code === 200) {
				this.cardImg = response.data.imageUrl;
			}
		},
	}
};
</script>
<style scoped>
	.photoContentLi{
		float: left;
		width: 50px;
		height: 50px;
		margin-left: 5px;
	}
	.photoContent ul{
		position: absolute;
		left:0;
		top:0
	}
	.photoContent img{
		width:50px;
		height:50px
	}
	.photoContent{
		overflow: hidden;
		position: relative;
		width: 385px;
		height:50px;
		float: left
	}
	.photoBox{
		width: 105%;
		height: 50px;
	}
	.scrollBtn{
		width: 20px;
		height: 50px;
		float: left;
	}
	.detail-row .row-photo-l .photo-add {
		width: 400px;
		height: 200px;
		text-align: center;
		background-color: #e5e5e6;
		color: #d9dbde;
		margin: 20px 0 0 0;
		position: relative;
		overflow: hidden;
		background-position: center center;
		background-size: 100% 100%;
		background-repeat: no-repeat;
		cursor: pointer;
	}

	.photo-add {
		cursor: pointer;
	}

	.el-input input[readonly='readonly'] {
		border: none;
	}
	.add-fn{
		width:400px
	}
	input[readonly = 'readonly']{
		border: none;
	}
	#progress {
		width: 400px;
		border: 1px solid #c0ccda;
		background-color: #e6e6e6;
		color: #d30312;
		height: 7px;
	}
	#progressBox{
		height: 7px;
		margin: -12px 0 20px 0;
	}
	.error-left{
		width:50%;
		margin-left: 215px;
	}

	#shopIdReadOnly .el-input__inner{
		border:none;
	}
	.imgupload{
		background-image: none;
		text-align: center;
		font-size: 12px;
		border-radius: 4px;
		color: #fff;
		background-color: #20a0ff;
		border-color: #20a0ff;
		width:68px;
		height:28px;
		line-height: 28px;
		cursor: pointer;
	}
</style>
