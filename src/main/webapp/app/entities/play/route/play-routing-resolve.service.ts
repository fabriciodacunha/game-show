import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlay, Play } from '../play.model';
import { PlayService } from '../service/play.service';

@Injectable({ providedIn: 'root' })
export class PlayRoutingResolveService implements Resolve<IPlay> {
  constructor(protected service: PlayService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlay> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((play: HttpResponse<Play>) => {
          if (play.body) {
            return of(play.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Play());
  }
}
