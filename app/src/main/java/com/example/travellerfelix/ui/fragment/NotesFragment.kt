package com.example.travellerfelix.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travellerfelix.adapter.NoteAdapter
import com.example.travellerfelix.bottomsheet.BottomSheetAddNoteFragment
import com.example.travellerfelix.data.local.database.TravelDatabase
import com.example.travellerfelix.data.local.model.Note
import com.example.travellerfelix.data.repository.NoteRepository
import com.example.travellerfelix.databinding.FragmentNotesBinding
import com.example.travellerfelix.viewmodel.NoteViewModel
import com.example.travellerfelix.viewmodel.NoteViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteAdapter: NoteAdapter

    private val viewModel: NoteViewModel by viewModels {
        val noteDao = TravelDatabase.getDatabase(requireContext()).noteDao()
        val repository = NoteRepository(noteDao)
        NoteViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteAdapter = NoteAdapter(
            notes = listOf(),
            onEditClick = { note ->
                BottomSheetAddNoteFragment(
                    onNoteAdded = { updatedNote ->
                        viewModel.updateNote(updatedNote)
                    },
                    existingNote = note
                ).show(parentFragmentManager, "BottomSheetEditNote")
            },
            onDeleteClick = { note ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Notu Sil")
                    .setMessage("Bu notu silmek istediğinize emin misiniz?")
                    .setPositiveButton("Evet") { _, _ ->
                        viewModel.deleteNote(note)
                    }
                    .setNegativeButton("İptal", null)
                    .show()
            }
        )

        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView2.adapter = noteAdapter

        lifecycleScope.launch {
            viewModel.notes.collectLatest { noteList ->
                if (noteList.isEmpty()) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.recyclerView2.visibility = View.GONE
                } else {
                    binding.emptyView.visibility = View.GONE
                    binding.recyclerView2.visibility = View.VISIBLE
                }
                noteAdapter.updateNotes(noteList)
            }
        }

        binding.imageView.setOnClickListener {
            BottomSheetAddNoteFragment(
                onNoteAdded = { newNote ->
                    viewModel.addNote(newNote)
                }
            ).show(parentFragmentManager, "BottomSheetAddNote")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}