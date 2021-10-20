import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEdicao, Edicao } from '../edicao.model';
import { EdicaoService } from '../service/edicao.service';

@Injectable({ providedIn: 'root' })
export class EdicaoRoutingResolveService implements Resolve<IEdicao> {
  constructor(protected service: EdicaoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEdicao> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((edicao: HttpResponse<Edicao>) => {
          if (edicao.body) {
            return of(edicao.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Edicao());
  }
}
