/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { GradingAppTestModule } from '../../../test.module';
import { FdaiNummerUpdateComponent } from 'app/entities/fdai-nummer/fdai-nummer-update.component';
import { FdaiNummerService } from 'app/entities/fdai-nummer/fdai-nummer.service';
import { FdaiNummer } from 'app/shared/model/fdai-nummer.model';

describe('Component Tests', () => {
    describe('FdaiNummer Management Update Component', () => {
        let comp: FdaiNummerUpdateComponent;
        let fixture: ComponentFixture<FdaiNummerUpdateComponent>;
        let service: FdaiNummerService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GradingAppTestModule],
                declarations: [FdaiNummerUpdateComponent]
            })
                .overrideTemplate(FdaiNummerUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FdaiNummerUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FdaiNummerService);
        });
    });
});
