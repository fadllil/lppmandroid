package com.example.fadllil.lppmandroid.model

import com.google.gson.annotations.SerializedName

data class Info(

	@field:SerializedName("url_file_info")
	val urlFileInfo: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("file_info")
	val fileInfo: String? = null,

	@field:SerializedName("judul_info")
	val judulInfo: String? = null,

	@field:SerializedName("keterangan_info")
	val keteranganInfo: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_info")
	val idInfo: Int? = null
)