import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEdicao } from '../edicao.model';
import { EdicaoService } from '../service/edicao.service';

@Component({
  templateUrl: './edicao-delete-dialog.component.html',
})
export class EdicaoDeleteDialogComponent {
  edicao?: IEdicao;

  constructor(protected edicaoService: EdicaoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.edicaoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
