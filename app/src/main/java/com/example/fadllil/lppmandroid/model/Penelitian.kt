package com.example.fadllil.lppmandroid.model

import com.google.gson.annotations.SerializedName

data class Penelitian(

	@field:SerializedName("sumber_dana")
	val sumberDana: String? = null,

	@field:SerializedName("penulis")
	val penulis: String? = null,

	@field:SerializedName("id_rak")
	val idRak: Int? = null,

	@field:SerializedName("tahun")
	val tahun: Int? = null,

	@field:SerializedName("nama_rak")
	val namaRak: String? = null,

	@field:SerializedName("id_fakultas")
	val idFakultas: Int? = null,

	@field:SerializedName("id_cluster")
	val idCluster: Int? = null,

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("nama_fakultas")
	val namaFakultas: String? = null,

	@field:SerializedName("no_sk")
	val noSk: String? = null,

	@field:SerializedName("pendanaan")
	val pendanaan: Int? = null,

	@field:SerializedName("nama_cluster")
	val namaCluster: String? = null,

	@field:SerializedName("judul")
	val judul: String? = null
)