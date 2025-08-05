// Simple fallback implementation to avoid React import issues
import * as React from "react";

// Fallback implementation
function useToast() {
  try {
    if (!React || !React.useState) {
      console.warn('React hooks not available, using fallback toast implementation');
      return {
        toasts: [],
        toast: (props) => {
          console.log('Toast (fallback):', props);
          return { id: 'fallback', dismiss: () => {}, update: () => {} };
        },
        dismiss: () => {},
      };
    }

  // Original implementation
  const TOAST_LIMIT = 1;
  const listeners = [];
  let memoryState = { toasts: [] };
  let count = 0;

  function genId() {
    count = (count + 1) % Number.MAX_SAFE_INTEGER;
    return count.toString();
  }

  const toastTimeouts = new Map();

  const addToRemoveQueue = (toastId) => {
    if (toastTimeouts.has(toastId)) {
      return;
    }

    const timeout = setTimeout(() => {
      toastTimeouts.delete(toastId);
      dispatch({
        type: "REMOVE_TOAST",
        toastId: toastId,
      });
    }, 5000);

    toastTimeouts.set(toastId, timeout);
  };

  const reducer = (state, action) => {
    switch (action.type) {
      case "ADD_TOAST":
        return {
          ...state,
          toasts: [action.toast, ...state.toasts].slice(0, TOAST_LIMIT),
        };

      case "UPDATE_TOAST":
        return {
          ...state,
          toasts: state.toasts.map((t) =>
            t.id === action.toast.id ? { ...t, ...action.toast } : t
          ),
        };

      case "DISMISS_TOAST": {
        const { toastId } = action;

        if (toastId) {
          addToRemoveQueue(toastId);
        } else {
          state.toasts.forEach((toast) => {
            addToRemoveQueue(toast.id);
          });
        }

        return {
          ...state,
          toasts: state.toasts.map((t) =>
            t.id === toastId || toastId === undefined
              ? {
                  ...t,
                  open: false,
                }
              : t
          ),
        };
      }
      case "REMOVE_TOAST":
        if (action.toastId === undefined) {
          return {
            ...state,
            toasts: [],
          };
        }
        return {
          ...state,
          toasts: state.toasts.filter((t) => t.id !== action.toastId),
        };
    }
  };

  function dispatch(action) {
    memoryState = reducer(memoryState, action);
    listeners.forEach((listener) => {
      listener(memoryState);
    });
  }

  function toast(props) {
    const id = genId();

    const update = (props) =>
      dispatch({
        type: "UPDATE_TOAST",
        toast: { ...props, id },
      });
    const dismiss = () => dispatch({ type: "DISMISS_TOAST", toastId: id });

    dispatch({
      type: "ADD_TOAST",
      toast: {
        ...props,
        id,
        open: true,
        onOpenChange: (open) => {
          if (!open && !window.getSelection()?.toString()) {
            dismiss();
          }
        },
      },
    });

    return {
      id: id,
      dismiss,
      update,
    };
  }

  const [state, setState] = React.useState(memoryState);

  React.useEffect(() => {
    listeners.push(setState);
    return () => {
      const index = listeners.indexOf(setState);
      if (index > -1) {
        listeners.splice(index, 1);
      }
    };
  }, [state]);

  return {
    ...state,
    toast,
    dismiss: (toastId) => dispatch({ type: "DISMISS_TOAST", toastId }),
  };
  } catch (error) {
    console.error('Error in useToast hook:', error);
    return {
      toasts: [],
      toast: (props) => {
        console.log('Toast (fallback):', props);
        return { id: 'fallback', dismiss: () => {}, update: () => {} };
      },
      dismiss: () => {},
    };
  }
}

export { useToast };
export const toast = (props) => {
  console.log('Toast called:', props);
  return { id: 'fallback', dismiss: () => {}, update: () => {} };
};
