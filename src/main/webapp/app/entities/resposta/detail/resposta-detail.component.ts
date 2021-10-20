import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResposta } from '../resposta.model';

@Component({
  selector: 'jhi-resposta-detail',
  templateUrl: './resposta-detail.component.html',
})
export class RespostaDetailComponent implements OnInit {
  resposta: IResposta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resposta }) => {
      this.resposta = resposta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
