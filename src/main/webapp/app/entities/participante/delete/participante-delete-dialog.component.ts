import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IParticipante } from '../participante.model';
import { ParticipanteService } from '../service/participante.service';

@Component({
  templateUrl: './participante-delete-dialog.component.html',
})
export class ParticipanteDeleteDialogComponent {
  participante?: IParticipante;

  constructor(protected participanteService: ParticipanteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.participanteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
