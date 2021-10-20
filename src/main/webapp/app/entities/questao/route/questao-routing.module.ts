import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuestaoComponent } from '../list/questao.component';
import { QuestaoDetailComponent } from '../detail/questao-detail.component';
import { QuestaoUpdateComponent } from '../update/questao-update.component';
import { QuestaoRoutingResolveService } from './questao-routing-resolve.service';

const questaoRoute: Routes = [
  {
    path: '',
    component: QuestaoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuestaoDetailComponent,
    resolve: {
      questao: QuestaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuestaoUpdateComponent,
    resolve: {
      questao: QuestaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuestaoUpdateComponent,
    resolve: {
      questao: QuestaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(questaoRoute)],
  exports: [RouterModule],
})
export class QuestaoRoutingModule {}
