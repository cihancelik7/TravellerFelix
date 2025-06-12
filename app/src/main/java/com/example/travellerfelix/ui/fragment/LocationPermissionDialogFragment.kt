package com.example.travellerfelix.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.travellerfelix.R
import com.example.travellerfelix.utils.PreferenceHelper

class LocationPermissionDialogFragment(private val onRequestPermission: () -> Unit) :
    DialogFragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_location_permission,null)

        val btnAllow = view.findViewById<Button>(R.id.btnAllow)
        val btnLater = view.findViewById<Button>(R.id.btnLater)

        btnAllow.setOnClickListener {
            dismiss()
            onRequestPermission()
        }
        btnLater.setOnClickListener {
            PreferenceHelper.setUserDeferredLocationPermission(requireContext(), true)
            dismiss()
        }
        builder.setView(view)
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent) // ✨ bu çok önemli
    }


}