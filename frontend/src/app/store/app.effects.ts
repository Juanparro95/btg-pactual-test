import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { map, mergeMap, catchError } from 'rxjs/operators';
import { EMPTY } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import * as AppActions from './app.actions';

@Injectable()
export class AppEffects {

  constructor(
    private actions$: Actions,
    private http: HttpClient
  ) {}

  loadData$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AppActions.loadData),
      mergeMap(() => this.http.get('API_ENDPOINT')
        .pipe(
          map(data => AppActions.loadDataSuccess({ data })),
          catchError(() => EMPTY)
        ))
    )
  );
}
