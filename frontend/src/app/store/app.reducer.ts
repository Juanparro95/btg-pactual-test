import { createReducer, on } from '@ngrx/store';
import { loadDataSuccess } from './app.actions';
import { AppState, initialAppState } from './app.state';

// Reducer que maneja el estado
export const appReducer = createReducer(
  initialAppState, 
  on(loadDataSuccess, (state, { data }) => ({ ...state, data }))  
);
