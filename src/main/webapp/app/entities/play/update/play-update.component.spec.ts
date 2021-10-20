jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PlayService } from '../service/play.service';
import { IPlay, Play } from '../play.model';

import { PlayUpdateComponent } from './play-update.component';

describe('Play Management Update Component', () => {
  let comp: PlayUpdateComponent;
  let fixture: ComponentFixture<PlayUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playService: PlayService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PlayUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PlayUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlayUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playService = TestBed.inject(PlayService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const play: IPlay = { id: 456 };

      activatedRoute.data = of({ play });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(play));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Play>>();
      const play = { id: 123 };
      jest.spyOn(playService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ play });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: play }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(playService.update).toHaveBeenCalledWith(play);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Play>>();
      const play = new Play();
      jest.spyOn(playService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ play });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: play }));
      saveSubject.complete();

      // THEN
      expect(playService.create).toHaveBeenCalledWith(play);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Play>>();
      const play = { id: 123 };
      jest.spyOn(playService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ play });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playService.update).toHaveBeenCalledWith(play);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
