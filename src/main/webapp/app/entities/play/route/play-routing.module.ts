import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlayComponent } from '../list/play.component';
import { PlayDetailComponent } from '../detail/play-detail.component';
import { PlayUpdateComponent } from '../update/play-update.component';
import { PlayRoutingResolveService } from './play-routing-resolve.service';

const playRoute: Routes = [
  {
    path: '',
    component: PlayComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlayDetailComponent,
    resolve: {
      play: PlayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlayUpdateComponent,
    resolve: {
      play: PlayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlayUpdateComponent,
    resolve: {
      play: PlayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(playRoute)],
  exports: [RouterModule],
})
export class PlayRoutingModule {}
