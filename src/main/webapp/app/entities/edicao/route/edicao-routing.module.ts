import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EdicaoComponent } from '../list/edicao.component';
import { EdicaoDetailComponent } from '../detail/edicao-detail.component';
import { EdicaoUpdateComponent } from '../update/edicao-update.component';
import { EdicaoRoutingResolveService } from './edicao-routing-resolve.service';

const edicaoRoute: Routes = [
  {
    path: '',
    component: EdicaoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EdicaoDetailComponent,
    resolve: {
      edicao: EdicaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EdicaoUpdateComponent,
    resolve: {
      edicao: EdicaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EdicaoUpdateComponent,
    resolve: {
      edicao: EdicaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(edicaoRoute)],
  exports: [RouterModule],
})
export class EdicaoRoutingModule {}
