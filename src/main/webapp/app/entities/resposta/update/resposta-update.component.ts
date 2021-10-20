import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IResposta, Resposta } from '../resposta.model';
import { RespostaService } from '../service/resposta.service';
import { IQuestao } from 'app/entities/questao/questao.model';
import { QuestaoService } from 'app/entities/questao/service/questao.service';
import { IParticipante } from 'app/entities/participante/participante.model';
import { ParticipanteService } from 'app/entities/participante/service/participante.service';
import { IPlay } from 'app/entities/play/play.model';
import { PlayService } from 'app/entities/play/service/play.service';
import { Alternativa } from 'app/entities/enumerations/alternativa.model';

@Component({
  selector: 'jhi-resposta-update',
  templateUrl: './resposta-update.component.html',
})
export class RespostaUpdateComponent implements OnInit {
  isSaving = false;
  alternativaValues = Object.keys(Alternativa);

  questaosSharedCollection: IQuestao[] = [];
  participantesSharedCollection: IParticipante[] = [];
  playsSharedCollection: IPlay[] = [];

  editForm = this.fb.group({
    id: [],
    respostaData: [],
    respostaAlternativa: [],
    respostaCerta: [],
    questao: [],
    participante: [],
    play: [],
  });

  constructor(
    protected respostaService: RespostaService,
    protected questaoService: QuestaoService,
    protected participanteService: ParticipanteService,
    protected playService: PlayService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resposta }) => {
      if (resposta.id === undefined) {
        const today = dayjs().startOf('day');
        resposta.respostaData = today;
      }

      this.updateForm(resposta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resposta = this.createFromForm();
    if (resposta.id !== undefined) {
      this.subscribeToSaveResponse(this.respostaService.update(resposta));
    } else {
      this.subscribeToSaveResponse(this.respostaService.create(resposta));
    }
  }

  trackQuestaoById(index: number, item: IQuestao): number {
    return item.id!;
  }

  trackParticipanteById(index: number, item: IParticipante): number {
    return item.id!;
  }

  trackPlayById(index: number, item: IPlay): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResposta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(resposta: IResposta): void {
    this.editForm.patchValue({
      id: resposta.id,
      respostaData: resposta.respostaData ? resposta.respostaData.format(DATE_TIME_FORMAT) : null,
      respostaAlternativa: resposta.respostaAlternativa,
      respostaCerta: resposta.respostaCerta,
      questao: resposta.questao,
      participante: resposta.participante,
      play: resposta.play,
    });

    this.questaosSharedCollection = this.questaoService.addQuestaoToCollectionIfMissing(this.questaosSharedCollection, resposta.questao);
    this.participantesSharedCollection = this.participanteService.addParticipanteToCollectionIfMissing(
      this.participantesSharedCollection,
      resposta.participante
    );
    this.playsSharedCollection = this.playService.addPlayToCollectionIfMissing(this.playsSharedCollection, resposta.play);
  }

  protected loadRelationshipsOptions(): void {
    this.questaoService
      .query()
      .pipe(map((res: HttpResponse<IQuestao[]>) => res.body ?? []))
      .pipe(
        map((questaos: IQuestao[]) => this.questaoService.addQuestaoToCollectionIfMissing(questaos, this.editForm.get('questao')!.value))
      )
      .subscribe((questaos: IQuestao[]) => (this.questaosSharedCollection = questaos));

    this.participanteService
      .query()
      .pipe(map((res: HttpResponse<IParticipante[]>) => res.body ?? []))
      .pipe(
        map((participantes: IParticipante[]) =>
          this.participanteService.addParticipanteToCollectionIfMissing(participantes, this.editForm.get('participante')!.value)
        )
      )
      .subscribe((participantes: IParticipante[]) => (this.participantesSharedCollection = participantes));

    this.playService
      .query()
      .pipe(map((res: HttpResponse<IPlay[]>) => res.body ?? []))
      .pipe(map((plays: IPlay[]) => this.playService.addPlayToCollectionIfMissing(plays, this.editForm.get('play')!.value)))
      .subscribe((plays: IPlay[]) => (this.playsSharedCollection = plays));
  }

  protected createFromForm(): IResposta {
    return {
      ...new Resposta(),
      id: this.editForm.get(['id'])!.value,
      respostaData: this.editForm.get(['respostaData'])!.value
        ? dayjs(this.editForm.get(['respostaData'])!.value, DATE_TIME_FORMAT)
        : undefined,
      respostaAlternativa: this.editForm.get(['respostaAlternativa'])!.value,
      respostaCerta: this.editForm.get(['respostaCerta'])!.value,
      questao: this.editForm.get(['questao'])!.value,
      participante: this.editForm.get(['participante'])!.value,
      play: this.editForm.get(['play'])!.value,
    };
  }
}
