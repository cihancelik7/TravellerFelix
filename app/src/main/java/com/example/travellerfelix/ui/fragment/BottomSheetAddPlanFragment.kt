package com.example.travellerfelix.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.travellerfelix.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetAddPlanFragment: BottomSheetDialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_plan,container,false)
    }


}