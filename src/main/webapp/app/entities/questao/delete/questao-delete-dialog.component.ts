import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuestao } from '../questao.model';
import { QuestaoService } from '../service/questao.service';

@Component({
  templateUrl: './questao-delete-dialog.component.html',
})
export class QuestaoDeleteDialogComponent {
  questao?: IQuestao;

  constructor(protected questaoService: QuestaoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.questaoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
