package com.example.travellerfelix.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.travellerfelix.R
import com.example.travellerfelix.data.local.model.Note
import com.example.travellerfelix.databinding.FragmentBottomSheetAddNoteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class BottomSheetAddNoteFragment(
    private val onNoteAdded: (Note) -> Unit,
    private val existingNote: Note? = null
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetAddNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBottomSheetAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        existingNote?.let { note ->
            binding.noteContentEditText.setText(note.content)
            binding.saveNoteButton.text = "Güncelle"
        }

        binding.saveNoteButton.setOnClickListener {
            val content = binding.noteContentEditText.text.toString().trim()
            if (content.isNotEmpty()) {
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val updatedNote = existingNote?.copy(
                    content = content,
                    createdAt = dateFormat.format(Date())
                ) ?: Note(
                    content = content,
                    createdAt = dateFormat.format(Date())
                )

                onNoteAdded(updatedNote)
                dismiss()
            } else {
                binding.noteContentEditText.error = "Lütfen bir not girin."
            }
        }

        binding.cancelNoteButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}