import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParticipante } from '../participante.model';

@Component({
  selector: 'jhi-participante-detail',
  templateUrl: './participante-detail.component.html',
})
export class ParticipanteDetailComponent implements OnInit {
  participante: IParticipante | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ participante }) => {
      this.participante = participante;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
