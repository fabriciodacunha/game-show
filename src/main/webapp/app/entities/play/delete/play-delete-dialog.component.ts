import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlay } from '../play.model';
import { PlayService } from '../service/play.service';

@Component({
  templateUrl: './play-delete-dialog.component.html',
})
export class PlayDeleteDialogComponent {
  play?: IPlay;

  constructor(protected playService: PlayService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.playService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
