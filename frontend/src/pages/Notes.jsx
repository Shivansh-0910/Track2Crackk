import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { Badge } from "@/components/ui/badge"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Label } from "@/components/ui/label"
import {
  Search,
  Plus,
  Edit,
  Trash2,
  Tag,
  Calendar,
  StickyNote,
  Code,
  BookOpen
} from "lucide-react"
import { getNotes, createNote, updateNote, deleteNote } from "@/api/notes"
import { useToast } from "@/hooks/useToast"

export function Notes() {
  const [notes, setNotes] = useState([])
  const [filteredNotes, setFilteredNotes] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedTag, setSelectedTag] = useState('')
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false)
  const [editingNote, setEditingNote] = useState(null)
  const [formData, setFormData] = useState({
    title: '',
    content: '',
    tags: [],
    tagInput: ''
  })
  const { toast } = useToast()

  useEffect(() => {
    fetchNotes()
  }, [])

  useEffect(() => {
    let filtered = notes.filter(note =>
      note.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      note.content.toLowerCase().includes(searchTerm.toLowerCase()) ||
      note.tags.some(tag => tag.toLowerCase().includes(searchTerm.toLowerCase()))
    )

    if (selectedTag) {
      filtered = filtered.filter(note => note.tags.includes(selectedTag))
    }

    setFilteredNotes(filtered)
  }, [notes, searchTerm, selectedTag])

  const fetchNotes = async () => {
    try {
      console.log('Fetching notes...')
      const response = await getNotes()
      const notesData = response.notes
      setNotes(notesData)
      setFilteredNotes(notesData)
      console.log('Notes loaded successfully')
    } catch (error) {
      console.error('Error fetching notes:', error)
      toast({
        title: "Error",
        description: "Failed to load notes",
        variant: "destructive"
      })
    } finally {
      setLoading(false)
    }
  }

  const handleCreateNote = async () => {
    if (!formData.title.trim() || !formData.content.trim()) {
      toast({
        title: "Error",
        description: "Please fill in title and content",
        variant: "destructive"
      })
      return
    }

    try {
      console.log('Creating note:', formData)
      const response = await createNote({
        title: formData.title,
        content: formData.content,
        tags: formData.tags
      })

      const newNote = {
        _id: response.note._id,
        title: formData.title,
        content: formData.content,
        tags: formData.tags,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }

      setNotes(prev => [newNote, ...prev])
      resetForm()
      setIsCreateDialogOpen(false)
      toast({
        title: "Success",
        description: "Note created successfully",
      })
    } catch (error) {
      console.error('Error creating note:', error)
      toast({
        title: "Error",
        description: "Failed to create note",
        variant: "destructive"
      })
    }
  }

  const handleUpdateNote = async () => {
    if (!editingNote || !formData.title.trim() || !formData.content.trim()) {
      toast({
        title: "Error",
        description: "Please fill in title and content",
        variant: "destructive"
      })
      return
    }

    try {
      console.log('Updating note:', editingNote._id, formData)
      await updateNote(editingNote._id, {
        title: formData.title,
        content: formData.content,
        tags: formData.tags
      })

      setNotes(prev => prev.map(note =>
        note._id === editingNote._id
          ? { ...note, title: formData.title, content: formData.content, tags: formData.tags, updatedAt: new Date().toISOString() }
          : note
      ))

      resetForm()
      setEditingNote(null)
      toast({
        title: "Success",
        description: "Note updated successfully",
      })
    } catch (error) {
      console.error('Error updating note:', error)
      toast({
        title: "Error",
        description: "Failed to update note",
        variant: "destructive"
      })
    }
  }

  const handleDeleteNote = async (noteId) => {
    try {
      console.log('Deleting note:', noteId)
      await deleteNote(noteId)
      setNotes(prev => prev.filter(note => note._id !== noteId))
      toast({
        title: "Success",
        description: "Note deleted successfully",
      })
    } catch (error) {
      console.error('Error deleting note:', error)
      toast({
        title: "Error",
        description: "Failed to delete note",
        variant: "destructive"
      })
    }
  }

  const resetForm = () => {
    setFormData({
      title: '',
      content: '',
      tags: [],
      tagInput: ''
    })
  }

  const startEditing = (note) => {
    setEditingNote(note)
    setFormData({
      title: note.title,
      content: note.content,
      tags: note.tags,
      tagInput: ''
    })
  }

  const addTag = () => {
    if (formData.tagInput.trim() && !formData.tags.includes(formData.tagInput.trim())) {
      setFormData(prev => ({
        ...prev,
        tags: [...prev.tags, prev.tagInput.trim()],
        tagInput: ''
      }))
    }
  }

  const removeTag = (tagToRemove) => {
    setFormData(prev => ({
      ...prev,
      tags: prev.tags.filter(tag => tag !== tagToRemove)
    }))
  }

  const getAllTags = () => {
    const allTags = new Set()
    notes.forEach(note => {
      note.tags.forEach(tag => allTags.add(tag))
    })
    return Array.from(allTags)
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6 animate-in fade-in-50 duration-500">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Notes & Solutions</h1>
          <p className="text-gray-600 dark:text-gray-400 mt-1">
            Keep track of your problem-solving approaches and insights
          </p>
        </div>
        <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
          <DialogTrigger asChild>
            <Button className="bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700">
              <Plus className="w-4 h-4 mr-2" />
              New Note
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-2xl bg-white dark:bg-gray-800">
            <DialogHeader>
              <DialogTitle>Create New Note</DialogTitle>
            </DialogHeader>
            <div className="space-y-4">
              <div>
                <Label htmlFor="title">Title</Label>
                <Input
                  id="title"
                  value={formData.title}
                  onChange={(e) => setFormData(prev => ({ ...prev, title: e.target.value }))}
                  placeholder="Enter note title..."
                />
              </div>
              <div>
                <Label htmlFor="content">Content</Label>
                <Textarea
                  id="content"
                  value={formData.content}
                  onChange={(e) => setFormData(prev => ({ ...prev, content: e.target.value }))}
                  placeholder="Write your note content..."
                  rows={8}
                />
              </div>
              <div>
                <Label htmlFor="tags">Tags</Label>
                <div className="flex gap-2 mb-2">
                  <Input
                    id="tags"
                    value={formData.tagInput}
                    onChange={(e) => setFormData(prev => ({ ...prev, tagInput: e.target.value }))}
                    placeholder="Add a tag..."
                    onKeyPress={(e) => e.key === 'Enter' && addTag()}
                  />
                  <Button type="button" onClick={addTag} variant="outline">
                    Add
                  </Button>
                </div>
                <div className="flex flex-wrap gap-2">
                  {formData.tags.map(tag => (
                    <Badge key={tag} variant="secondary" className="cursor-pointer" onClick={() => removeTag(tag)}>
                      {tag} ×
                    </Badge>
                  ))}
                </div>
              </div>
              <div className="flex justify-end gap-2">
                <Button variant="outline" onClick={() => setIsCreateDialogOpen(false)}>
                  Cancel
                </Button>
                <Button onClick={handleCreateNote}>
                  Create Note
                </Button>
              </div>
            </div>
          </DialogContent>
        </Dialog>
      </div>

      {/* Search and Filter */}
      <Card className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg">
        <CardContent className="p-6">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
              <Input
                placeholder="Search notes..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
            <div className="flex flex-wrap gap-2">
              <Button
                variant={selectedTag === '' ? 'default' : 'outline'}
                size="sm"
                onClick={() => setSelectedTag('')}
              >
                All
              </Button>
              {getAllTags().map(tag => (
                <Button
                  key={tag}
                  variant={selectedTag === tag ? 'default' : 'outline'}
                  size="sm"
                  onClick={() => setSelectedTag(selectedTag === tag ? '' : tag)}
                >
                  {tag}
                </Button>
              ))}
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Notes Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredNotes.map((note) => (
          <Card key={note._id} className="bg-white/70 dark:bg-gray-800/70 backdrop-blur-sm border-0 shadow-lg hover:shadow-xl transition-all duration-300">
            <CardHeader className="pb-3">
              <div className="flex items-start justify-between">
                <div className="flex-1">
                  <CardTitle className="text-lg line-clamp-2">{note.title}</CardTitle>
                  <div className="flex items-center gap-2 mt-2 text-sm text-gray-600 dark:text-gray-400">
                    <Calendar className="w-4 h-4" />
                    <span>{new Date(note.createdAt).toLocaleDateString()}</span>
                  </div>
                </div>
                <div className="flex items-center gap-1">
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => startEditing(note)}
                  >
                    <Edit className="w-4 h-4" />
                  </Button>
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => handleDeleteNote(note._id)}
                    className="text-red-600 hover:text-red-700"
                  >
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </div>
            </CardHeader>
            <CardContent className="space-y-3">
              <p className="text-gray-700 dark:text-gray-300 line-clamp-4 text-sm">
                {note.content}
              </p>
              {note.tags.length > 0 && (
                <div className="flex flex-wrap gap-1">
                  {note.tags.map(tag => (
                    <Badge key={tag} variant="secondary" className="text-xs">
                      {tag}
                    </Badge>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        ))}
      </div>

      {filteredNotes.length === 0 && (
        <div className="text-center py-12">
          <StickyNote className="w-12 h-12 text-gray-400 mx-auto mb-4" />
          <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">
            {notes.length === 0 ? 'No notes yet' : 'No notes found'}
          </h3>
          <p className="text-gray-600 dark:text-gray-400">
            {notes.length === 0 
              ? 'Create your first note to get started'
              : 'Try adjusting your search or filter criteria'
            }
          </p>
        </div>
      )}

      {/* Edit Note Dialog */}
      <Dialog open={!!editingNote} onOpenChange={(open) => !open && setEditingNote(null)}>
        <DialogContent className="max-w-2xl bg-white dark:bg-gray-800">
          <DialogHeader>
            <DialogTitle>Edit Note</DialogTitle>
          </DialogHeader>
          <div className="space-y-4">
            <div>
              <Label htmlFor="edit-title">Title</Label>
              <Input
                id="edit-title"
                value={formData.title}
                onChange={(e) => setFormData(prev => ({ ...prev, title: e.target.value }))}
                placeholder="Enter note title..."
              />
            </div>
            <div>
              <Label htmlFor="edit-content">Content</Label>
              <Textarea
                id="edit-content"
                value={formData.content}
                onChange={(e) => setFormData(prev => ({ ...prev, content: e.target.value }))}
                placeholder="Write your note content..."
                rows={8}
              />
            </div>
            <div>
              <Label htmlFor="edit-tags">Tags</Label>
              <div className="flex gap-2 mb-2">
                <Input
                  id="edit-tags"
                  value={formData.tagInput}
                  onChange={(e) => setFormData(prev => ({ ...prev, tagInput: e.target.value }))}
                  placeholder="Add a tag..."
                  onKeyPress={(e) => e.key === 'Enter' && addTag()}
                />
                <Button type="button" onClick={addTag} variant="outline">
                  Add
                </Button>
              </div>
              <div className="flex flex-wrap gap-2">
                {formData.tags.map(tag => (
                  <Badge key={tag} variant="secondary" className="cursor-pointer" onClick={() => removeTag(tag)}>
                    {tag} ×
                  </Badge>
                ))}
              </div>
            </div>
            <div className="flex justify-end gap-2">
              <Button variant="outline" onClick={() => setEditingNote(null)}>
                Cancel
              </Button>
              <Button onClick={handleUpdateNote}>
                Update Note
              </Button>
            </div>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  )
}