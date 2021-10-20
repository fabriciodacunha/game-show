import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { QuestaoComponent } from './list/questao.component';
import { QuestaoDetailComponent } from './detail/questao-detail.component';
import { QuestaoUpdateComponent } from './update/questao-update.component';
import { QuestaoDeleteDialogComponent } from './delete/questao-delete-dialog.component';
import { QuestaoRoutingModule } from './route/questao-routing.module';

@NgModule({
  imports: [SharedModule, QuestaoRoutingModule],
  declarations: [QuestaoComponent, QuestaoDetailComponent, QuestaoUpdateComponent, QuestaoDeleteDialogComponent],
  entryComponents: [QuestaoDeleteDialogComponent],
})
export class QuestaoModule {}
