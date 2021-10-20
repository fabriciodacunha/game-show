import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EdicaoComponent } from './list/edicao.component';
import { EdicaoDetailComponent } from './detail/edicao-detail.component';
import { EdicaoUpdateComponent } from './update/edicao-update.component';
import { EdicaoDeleteDialogComponent } from './delete/edicao-delete-dialog.component';
import { EdicaoRoutingModule } from './route/edicao-routing.module';

@NgModule({
  imports: [SharedModule, EdicaoRoutingModule],
  declarations: [EdicaoComponent, EdicaoDetailComponent, EdicaoUpdateComponent, EdicaoDeleteDialogComponent],
  entryComponents: [EdicaoDeleteDialogComponent],
})
export class EdicaoModule {}
