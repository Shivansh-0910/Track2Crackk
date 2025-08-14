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
  BookOpen,
  FileText,
  Clock,
  Filter
} from "lucide-react"
import { getNotes, createNote, updateNote, deleteNote } from "@/api/notes"
import { useToast } from "@/hooks/useToast"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue
} from "@/components/ui/select"

export function Notes() {
  const [notes, setNotes] = useState([])
  const [filteredNotes, setFilteredNotes] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedTag, setSelectedTag] = useState('all')
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

    if (selectedTag && selectedTag !== 'all') {
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
    if (!formData.title.trim() || !formData.content.trim()) {
      toast({
        title: "Error",
        description: "Please fill in title and content",
        variant: "destructive"
      })
      return
    }

    try {
      console.log('Updating note:', editingNote._id, formData)
      const response = await updateNote(editingNote._id, {
        title: formData.title,
        content: formData.content,
        tags: formData.tags
      })

      setNotes(prev => prev.map(note =>
        note._id === editingNote._id
          ? { ...note, ...response.note, updatedAt: new Date().toISOString() }
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
    if (!confirm('Are you sure you want to delete this note?')) return

    try {
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
      tags: [...note.tags],
      tagInput: ''
    })
  }

  const addTag = () => {
    if (formData.tagInput.trim() && !formData.tags.includes(formData.tagInput.trim())) {
      setFormData(prev => ({
        ...prev,
        tags: [...prev.tags, formData.tagInput.trim()],
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
        <div className="flex flex-col items-center gap-4">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
          <p className="text-muted-foreground">Loading your notes...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-8 animate-in fade-in-50 duration-500">
      {/* Header Section */}
      <div className="card-premium rounded-3xl p-8 text-white shadow-glow animate-fade-in-scale">
        <div className="absolute inset-0 rounded-3xl bg-gradient-to-br from-primary via-accent to-primary/80 opacity-90"></div>
        <div className="relative z-10 flex items-center justify-between">
          <div>
            <h1 className="text-4xl font-bold mb-3 bg-gradient-to-r from-white to-white/90 bg-clip-text text-transparent">
              📝 Study Notes
            </h1>
            <p className="text-lg text-white/90 font-medium">Organize your DSA learning with personal notes</p>
          </div>
          <div className="flex items-center gap-3">
            <div className="p-3 bg-white/20 rounded-full backdrop-blur-sm">
              <StickyNote className="w-6 h-6 text-white" />
            </div>
            <div className="text-right">
              <div className="text-2xl font-bold text-white">{notes.length}</div>
              <p className="text-sm text-white/80">notes created</p>
            </div>
          </div>
        </div>
      </div>

      {/* Search and Filter Controls */}
      <Card className="card-premium animate-fade-in-scale">
        <CardContent className="p-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
              <Input
                placeholder="Search notes..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10 input-premium"
              />
            </div>
            
            <Select value={selectedTag} onValueChange={setSelectedTag}>
              <SelectTrigger className="input-premium">
                <Filter className="w-4 h-4 mr-2" />
                <SelectValue placeholder="Filter by tag" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Tags</SelectItem>
                {getAllTags().map(tag => (
                  <SelectItem key={tag} value={tag}>{tag}</SelectItem>
                ))}
              </SelectContent>
            </Select>

            <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
              <DialogTrigger asChild>
                <Button className="w-full btn-premium text-white font-semibold">
                  <Plus className="w-4 h-4 mr-2" />
                  Create Note
                </Button>
              </DialogTrigger>
              <DialogContent className="sm:max-w-[600px]">
                <DialogHeader>
                  <DialogTitle className="flex items-center gap-2">
                    <Plus className="w-5 h-5" />
                    Create New Note
                  </DialogTitle>
                </DialogHeader>
                <div className="space-y-4">
                  <div>
                    <Label htmlFor="title" className="text-sm font-medium text-foreground">Title</Label>
                    <Input
                      id="title"
                      value={formData.title}
                      onChange={(e) => setFormData(prev => ({ ...prev, title: e.target.value }))}
                      className="input-premium"
                      placeholder="Enter note title"
                    />
                  </div>
                  <div>
                    <Label htmlFor="content" className="text-sm font-medium text-foreground">Content</Label>
                    <Textarea
                      id="content"
                      value={formData.content}
                      onChange={(e) => setFormData(prev => ({ ...prev, content: e.target.value }))}
                      className="input-premium min-h-[200px]"
                      placeholder="Write your note content..."
                    />
                  </div>
                  <div>
                    <Label className="text-sm font-medium text-foreground">Tags</Label>
                    <div className="flex gap-2 mb-2">
                      <Input
                        value={formData.tagInput}
                        onChange={(e) => setFormData(prev => ({ ...prev, tagInput: e.target.value }))}
                        onKeyPress={(e) => e.key === 'Enter' && addTag()}
                        className="input-premium"
                        placeholder="Add a tag"
                      />
                      <Button onClick={addTag} variant="outline" size="sm">
                        <Plus className="w-4 h-4" />
                      </Button>
                    </div>
                    <div className="flex flex-wrap gap-2">
                      {formData.tags.map(tag => (
                        <Badge key={tag} variant="secondary" className="flex items-center gap-1">
                          {tag}
                          <button
                            onClick={() => removeTag(tag)}
                            className="ml-1 hover:text-destructive"
                          >
                            ×
                          </button>
                        </Badge>
                      ))}
                    </div>
                  </div>
                  <div className="flex justify-end gap-2">
                    <Button variant="outline" onClick={() => setIsCreateDialogOpen(false)}>
                      Cancel
                    </Button>
                    <Button onClick={handleCreateNote} className="btn-premium text-white">
                      Create Note
                    </Button>
                  </div>
                </div>
              </DialogContent>
            </Dialog>
          </div>
        </CardContent>
      </Card>

      {/* Notes Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredNotes.map((note, index) => (
          <Card 
            key={note._id} 
            className="card-premium hover:shadow-glow transition-all duration-300 hover:scale-[1.02] animate-slide-in-up"
            style={{ animationDelay: `${index * 0.1}s` }}
          >
            <CardContent className="p-6">
              <div className="space-y-4">
                {/* Header */}
                <div className="flex items-start justify-between">
                  <div className="flex items-center gap-3">
                    <div className="p-2 bg-primary/10 rounded-lg">
                      <FileText className="w-5 h-5 text-primary" />
                    </div>
                    <div className="flex-1">
                      <h3 className="font-semibold text-foreground line-clamp-1">{note.title}</h3>
                      <p className="text-xs text-muted-foreground">
                        {new Date(note.createdAt).toLocaleDateString()}
                      </p>
                    </div>
                  </div>
                  <div className="flex items-center gap-1">
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => startEditing(note)}
                      className="hover:bg-primary/10 hover:text-primary"
                    >
                      <Edit className="w-4 h-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleDeleteNote(note._id)}
                      className="hover:bg-destructive/10 hover:text-destructive"
                    >
                      <Trash2 className="w-4 h-4" />
                    </Button>
                  </div>
                </div>

                {/* Content Preview */}
                <div className="space-y-2">
                  <p className="text-sm text-muted-foreground line-clamp-3">
                    {note.content}
                  </p>
                </div>

                {/* Tags */}
                {note.tags.length > 0 && (
                  <div className="flex flex-wrap gap-1">
                    {note.tags.map(tag => (
                      <Badge key={tag} variant="outline" className="text-xs">
                        <Tag className="w-3 h-3 mr-1" />
                        {tag}
                      </Badge>
                    ))}
                  </div>
                )}

                {/* Footer */}
                <div className="flex items-center justify-between pt-3 border-t border-border/30">
                  <div className="flex items-center gap-2 text-xs text-muted-foreground">
                    <Clock className="w-3 h-3" />
                    <span>
                      {note.updatedAt && note.updatedAt !== note.createdAt
                        ? `Updated ${new Date(note.updatedAt).toLocaleDateString()}`
                        : `Created ${new Date(note.createdAt).toLocaleDateString()}`
                      }
                    </span>
                  </div>
                  <div className="text-xs text-muted-foreground">
                    {note.content.length} chars
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Empty State */}
      {filteredNotes.length === 0 && (
        <Card className="card-premium">
          <CardContent className="p-12 text-center">
            <div className="flex flex-col items-center gap-4">
              <div className="p-4 bg-muted/30 rounded-full">
                <StickyNote className="w-8 h-8 text-muted-foreground" />
              </div>
              <div>
                <h3 className="text-lg font-semibold text-foreground mb-2">
                  {searchTerm || selectedTag !== 'all' ? 'No notes found' : 'No notes yet'}
                </h3>
                <p className="text-muted-foreground">
                  {searchTerm || selectedTag !== 'all' 
                    ? 'Try adjusting your search terms or filters'
                    : 'Create your first note to start organizing your DSA learning!'
                  }
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Edit Note Dialog */}
      {editingNote && (
        <Dialog open={!!editingNote} onOpenChange={() => setEditingNote(null)}>
          <DialogContent className="sm:max-w-[600px]">
            <DialogHeader>
              <DialogTitle className="flex items-center gap-2">
                <Edit className="w-5 h-5" />
                Edit Note
              </DialogTitle>
            </DialogHeader>
            <div className="space-y-4">
              <div>
                <Label htmlFor="edit-title" className="text-sm font-medium text-foreground">Title</Label>
                <Input
                  id="edit-title"
                  value={formData.title}
                  onChange={(e) => setFormData(prev => ({ ...prev, title: e.target.value }))}
                  className="input-premium"
                  placeholder="Enter note title"
                />
              </div>
              <div>
                <Label htmlFor="edit-content" className="text-sm font-medium text-foreground">Content</Label>
                <Textarea
                  id="edit-content"
                  value={formData.content}
                  onChange={(e) => setFormData(prev => ({ ...prev, content: e.target.value }))}
                  className="input-premium min-h-[200px]"
                  placeholder="Write your note content..."
                />
              </div>
              <div>
                <Label className="text-sm font-medium text-foreground">Tags</Label>
                <div className="flex gap-2 mb-2">
                  <Input
                    value={formData.tagInput}
                    onChange={(e) => setFormData(prev => ({ ...prev, tagInput: e.target.value }))}
                    onKeyPress={(e) => e.key === 'Enter' && addTag()}
                    className="input-premium"
                    placeholder="Add a tag"
                  />
                  <Button onClick={addTag} variant="outline" size="sm">
                    <Plus className="w-4 h-4" />
                  </Button>
                </div>
                <div className="flex flex-wrap gap-2">
                  {formData.tags.map(tag => (
                    <Badge key={tag} variant="secondary" className="flex items-center gap-1">
                      {tag}
                      <button
                        onClick={() => removeTag(tag)}
                        className="ml-1 hover:text-destructive"
                      >
                        ×
                      </button>
                    </Badge>
                  ))}
                </div>
              </div>
              <div className="flex justify-end gap-2">
                <Button variant="outline" onClick={() => setEditingNote(null)}>
                  Cancel
                </Button>
                <Button onClick={handleUpdateNote} className="btn-premium text-white">
                  Update Note
                </Button>
              </div>
            </div>
          </DialogContent>
        </Dialog>
      )}
    </div>
  )
}