import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PlayComponent } from './list/play.component';
import { PlayDetailComponent } from './detail/play-detail.component';
import { PlayUpdateComponent } from './update/play-update.component';
import { PlayDeleteDialogComponent } from './delete/play-delete-dialog.component';
import { PlayRoutingModule } from './route/play-routing.module';

@NgModule({
  imports: [SharedModule, PlayRoutingModule],
  declarations: [PlayComponent, PlayDetailComponent, PlayUpdateComponent, PlayDeleteDialogComponent],
  entryComponents: [PlayDeleteDialogComponent],
})
export class PlayModule {}
