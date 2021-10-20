import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IParticipante, Participante } from '../participante.model';
import { ParticipanteService } from '../service/participante.service';
import { IPlay } from 'app/entities/play/play.model';
import { PlayService } from 'app/entities/play/service/play.service';

@Component({
  selector: 'jhi-participante-update',
  templateUrl: './participante-update.component.html',
})
export class ParticipanteUpdateComponent implements OnInit {
  isSaving = false;

  playsSharedCollection: IPlay[] = [];

  editForm = this.fb.group({
    id: [],
    carastroUsuario: [],
    participanteNome: [],
    participanteEmail: [],
    participanteDataDeNascimento: [],
    play: [],
  });

  constructor(
    protected participanteService: ParticipanteService,
    protected playService: PlayService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ participante }) => {
      this.updateForm(participante);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const participante = this.createFromForm();
    if (participante.id !== undefined) {
      this.subscribeToSaveResponse(this.participanteService.update(participante));
    } else {
      this.subscribeToSaveResponse(this.participanteService.create(participante));
    }
  }

  trackPlayById(index: number, item: IPlay): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParticipante>>): void {
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

  protected updateForm(participante: IParticipante): void {
    this.editForm.patchValue({
      id: participante.id,
      carastroUsuario: participante.carastroUsuario,
      participanteNome: participante.participanteNome,
      participanteEmail: participante.participanteEmail,
      participanteDataDeNascimento: participante.participanteDataDeNascimento,
      play: participante.play,
    });

    this.playsSharedCollection = this.playService.addPlayToCollectionIfMissing(this.playsSharedCollection, participante.play);
  }

  protected loadRelationshipsOptions(): void {
    this.playService
      .query()
      .pipe(map((res: HttpResponse<IPlay[]>) => res.body ?? []))
      .pipe(map((plays: IPlay[]) => this.playService.addPlayToCollectionIfMissing(plays, this.editForm.get('play')!.value)))
      .subscribe((plays: IPlay[]) => (this.playsSharedCollection = plays));
  }

  protected createFromForm(): IParticipante {
    return {
      ...new Participante(),
      id: this.editForm.get(['id'])!.value,
      carastroUsuario: this.editForm.get(['carastroUsuario'])!.value,
      participanteNome: this.editForm.get(['participanteNome'])!.value,
      participanteEmail: this.editForm.get(['participanteEmail'])!.value,
      participanteDataDeNascimento: this.editForm.get(['participanteDataDeNascimento'])!.value,
      play: this.editForm.get(['play'])!.value,
    };
  }
}
