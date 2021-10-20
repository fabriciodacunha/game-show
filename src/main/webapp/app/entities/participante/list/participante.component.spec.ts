import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ParticipanteService } from '../service/participante.service';

import { ParticipanteComponent } from './participante.component';

describe('Participante Management Component', () => {
  let comp: ParticipanteComponent;
  let fixture: ComponentFixture<ParticipanteComponent>;
  let service: ParticipanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ParticipanteComponent],
    })
      .overrideTemplate(ParticipanteComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParticipanteComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ParticipanteService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.participantes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
