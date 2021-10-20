jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ParticipanteService } from '../service/participante.service';
import { IParticipante, Participante } from '../participante.model';
import { IPlay } from 'app/entities/play/play.model';
import { PlayService } from 'app/entities/play/service/play.service';

import { ParticipanteUpdateComponent } from './participante-update.component';

describe('Participante Management Update Component', () => {
  let comp: ParticipanteUpdateComponent;
  let fixture: ComponentFixture<ParticipanteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let participanteService: ParticipanteService;
  let playService: PlayService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ParticipanteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ParticipanteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParticipanteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    participanteService = TestBed.inject(ParticipanteService);
    playService = TestBed.inject(PlayService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Play query and add missing value', () => {
      const participante: IParticipante = { id: 456 };
      const play: IPlay = { id: 34736 };
      participante.play = play;

      const playCollection: IPlay[] = [{ id: 5186 }];
      jest.spyOn(playService, 'query').mockReturnValue(of(new HttpResponse({ body: playCollection })));
      const additionalPlays = [play];
      const expectedCollection: IPlay[] = [...additionalPlays, ...playCollection];
      jest.spyOn(playService, 'addPlayToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      expect(playService.query).toHaveBeenCalled();
      expect(playService.addPlayToCollectionIfMissing).toHaveBeenCalledWith(playCollection, ...additionalPlays);
      expect(comp.playsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const participante: IParticipante = { id: 456 };
      const play: IPlay = { id: 67405 };
      participante.play = play;

      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(participante));
      expect(comp.playsSharedCollection).toContain(play);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Participante>>();
      const participante = { id: 123 };
      jest.spyOn(participanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: participante }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(participanteService.update).toHaveBeenCalledWith(participante);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Participante>>();
      const participante = new Participante();
      jest.spyOn(participanteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: participante }));
      saveSubject.complete();

      // THEN
      expect(participanteService.create).toHaveBeenCalledWith(participante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Participante>>();
      const participante = { id: 123 };
      jest.spyOn(participanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(participanteService.update).toHaveBeenCalledWith(participante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPlayById', () => {
      it('Should return tracked Play primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlayById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
