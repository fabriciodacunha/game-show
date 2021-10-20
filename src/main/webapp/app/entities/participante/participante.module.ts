import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ParticipanteComponent } from './list/participante.component';
import { ParticipanteDetailComponent } from './detail/participante-detail.component';
import { ParticipanteUpdateComponent } from './update/participante-update.component';
import { ParticipanteDeleteDialogComponent } from './delete/participante-delete-dialog.component';
import { ParticipanteRoutingModule } from './route/participante-routing.module';

@NgModule({
  imports: [SharedModule, ParticipanteRoutingModule],
  declarations: [ParticipanteComponent, ParticipanteDetailComponent, ParticipanteUpdateComponent, ParticipanteDeleteDialogComponent],
  entryComponents: [ParticipanteDeleteDialogComponent],
})
export class ParticipanteModule {}
