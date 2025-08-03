import api from './api';

// Description: Get all user notes
// Endpoint: GET /api/notes
// Request: {}
// Response: { notes: Array<{ _id: string, title: string, content: string, tags: string[], problemId?: string, createdAt: string, updatedAt: string }> }
export const getNotes = () => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        notes: [
          {
            _id: '507f1f77bcf86cd799439025',
            title: 'Two Sum Approach',
            content: 'Use HashMap to store complement values. Time: O(n), Space: O(n)',
            tags: ['arrays', 'hashmap'],
            problemId: '507f1f77bcf86cd799439018',
            createdAt: '2024-01-15T10:30:00Z',
            updatedAt: '2024-01-15T10:30:00Z'
          },
          {
            _id: '507f1f77bcf86cd799439026',
            title: 'Binary Tree Traversal Patterns',
            content: 'Inorder: Left -> Root -> Right\nPreorder: Root -> Left -> Right\nPostorder: Left -> Right -> Root',
            tags: ['trees', 'traversal'],
            createdAt: '2024-01-14T15:20:00Z',
            updatedAt: '2024-01-14T15:20:00Z'
          }
        ]
      });
    }, 500);
  });
  // try {
  //   return await api.get('/api/notes');
  // } catch (error) {
  //   throw new Error(error?.response?.data?.message || error.message);
  // }
};

// Description: Create a new note
// Endpoint: POST /api/notes
// Request: { title: string, content: string, tags: string[], problemId?: string }
// Response: { success: boolean, message: string, note: { _id: string } }
export const createNote = (data) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        success: true,
        message: 'Note created successfully',
        note: { _id: '507f1f77bcf86cd799439027' }
      });
    }, 500);
  });
  // try {
  //   return await api.post('/api/notes', data);
  // } catch (error) {
  //   throw new Error(error?.response?.data?.message || error.message);
  // }
};

// Description: Update a note
// Endpoint: PUT /api/notes/:noteId
// Request: { title?: string, content?: string, tags?: string[] }
// Response: { success: boolean, message: string }
export const updateNote = (noteId, data) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({ success: true, message: 'Note updated successfully' });
    }, 500);
  });
  // try {
  //   return await api.put(`/api/notes/${noteId}`, data);
  // } catch (error) {
  //   throw new Error(error?.response?.data?.message || error.message);
  // }
};

// Description: Delete a note
// Endpoint: DELETE /api/notes/:noteId
// Request: {}
// Response: { success: boolean, message: string }
export const deleteNote = (noteId) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({ success: true, message: 'Note deleted successfully' });
    }, 300);
  });
  // try {
  //   return await api.delete(`/api/notes/${noteId}`);
  // } catch (error) {
  //   throw new Error(error?.response?.data?.message || error.message);
  // }
};