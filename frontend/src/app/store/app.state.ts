import { createSelector } from '@ngrx/store';

// Aquí defines las interfaces del estado
export interface AppState {
  user: UserState;
  config: ConfigState;
  data: any[];
}


// Define los sub-estados (UserState, ConfigState, etc.)
export interface UserState {
  username: string;
  loggedIn: boolean;
}

export interface ConfigState {
  theme: string;
}

// Estado inicial
export const initialUserState: UserState = {
  username: '',
  loggedIn: false,
};

export const initialConfigState: ConfigState = {
  theme: 'light',
};

// Estado inicial global
export const initialAppState: AppState = {
  user: initialUserState,
  config: initialConfigState,
  data: [],
};

// Selectores globales
export const selectUser = (state: AppState) => state.user;
export const selectConfig = (state: AppState) => state.config;

export const selectUsername = createSelector(
  selectUser,
  (state: UserState) => state.username
);

export const selectTheme = createSelector(
  selectConfig,
  (state: ConfigState) => state.theme
);

export const selectApp = (state: AppState) => state.data;

export const selectData = createSelector(
  selectApp,
  (data) => data
);