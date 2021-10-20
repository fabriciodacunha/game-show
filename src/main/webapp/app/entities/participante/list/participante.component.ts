import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IParticipante } from '../participante.model';
import { ParticipanteService } from '../service/participante.service';
import { ParticipanteDeleteDialogComponent } from '../delete/participante-delete-dialog.component';

@Component({
  selector: 'jhi-participante',
  templateUrl: './participante.component.html',
})
export class ParticipanteComponent implements OnInit {
  participantes?: IParticipante[];
  isLoading = false;

  constructor(protected participanteService: ParticipanteService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.participanteService.query().subscribe(
      (res: HttpResponse<IParticipante[]>) => {
        this.isLoading = false;
        this.participantes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IParticipante): number {
    return item.id!;
  }

  delete(participante: IParticipante): void {
    const modalRef = this.modalService.open(ParticipanteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.participante = participante;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
