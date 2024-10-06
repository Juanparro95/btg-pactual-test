import { createAction } from '@ngrx/store';

export const loadData = createAction('[App] Load Data');
export const loadDataSuccess = createAction('[App] Load Data Success', (data: any) => ({ data }));
