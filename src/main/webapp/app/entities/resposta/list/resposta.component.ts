import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IResposta } from '../resposta.model';
import { RespostaService } from '../service/resposta.service';
import { RespostaDeleteDialogComponent } from '../delete/resposta-delete-dialog.component';

@Component({
  selector: 'jhi-resposta',
  templateUrl: './resposta.component.html',
})
export class RespostaComponent implements OnInit {
  respostas?: IResposta[];
  isLoading = false;

  constructor(protected respostaService: RespostaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.respostaService.query().subscribe(
      (res: HttpResponse<IResposta[]>) => {
        this.isLoading = false;
        this.respostas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IResposta): number {
    return item.id!;
  }

  delete(resposta: IResposta): void {
    const modalRef = this.modalService.open(RespostaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.resposta = resposta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
