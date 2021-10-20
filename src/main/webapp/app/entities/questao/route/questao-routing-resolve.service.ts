import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuestao, Questao } from '../questao.model';
import { QuestaoService } from '../service/questao.service';

@Injectable({ providedIn: 'root' })
export class QuestaoRoutingResolveService implements Resolve<IQuestao> {
  constructor(protected service: QuestaoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuestao> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((questao: HttpResponse<Questao>) => {
          if (questao.body) {
            return of(questao.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Questao());
  }
}
